package com.dc.feeds.service;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.pojo.Feeds;
import com.dc.commons.model.vo.FeedsVO;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.feeds.mapper.FeedsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedsService {

    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Value("${service.name.cs-follow-server}")
    private String followServerName;
    @Value("${service.name.cs-diners-server}")
    private String dinersServerName;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private FeedsMapper feedsMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根據時間由近至遠，每次查詢 20 條 Feed
     *
     * @param page
     * @param accessToken
     * @return
     */
    public List<FeedsVO> selectForPage(Integer page, String accessToken) {
        if (page == null) {
            page = 1;
        }
        // 獲取登錄用戶
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 我關注的好友的 Feedkey
        String key = RedisKeyConstant.following_feeds.getKey() + dinerInfo.getId();
        // SortedSet 的 ZREVRANGE 命令是閉區間
        long start = (page - 1) * ApiConstant.PAGE_SIZE;
        long end = page * ApiConstant.PAGE_SIZE - 1;
        Set<Integer> feedIds = redisTemplate.opsForZSet().reverseRange(key, start, end);
        if (feedIds == null || feedIds.isEmpty()) {
            return Lists.newArrayList();
        }
        // 根據多主鍵查詢 Feed
        List<Feeds> feeds = feedsMapper.findFeedsByIds(feedIds);
        // 初始化關注好友 ID 集合
        List<Integer> followingDinerIds = new ArrayList<>();
        // 添加用戶 ID 至集合，順帶將 Feeds 轉為 Vo 對象
        List<FeedsVO> feedsVOS = feeds.stream().map(feed -> {
            FeedsVO feedsVO = new FeedsVO();
            BeanUtil.copyProperties(feed, feedsVO);
            // 添加用戶 ID
            followingDinerIds.add(feed.getFkDinerId());
            return feedsVO;
        }).collect(Collectors.toList());
        // 遠程調用獲取 Feed 中用戶信息
        ResultInfo resultInfo = restTemplate.getForObject(dinersServerName + "findByIds?access_token=${accessToken}&ids={ids}",
                ResultInfo.class, accessToken, followingDinerIds);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        List<LinkedHashMap> dinerInfoMaps = (ArrayList) resultInfo.getData();
        // 構建一個 key 為用戶 ID，value 為 ShortDinerInfo 的 Map
        Map<Integer, ShortDinerInfo> dinerInfos = dinerInfoMaps.stream()
                .collect(Collectors.toMap(
                        // key
                        diner -> (Integer) diner.get("id"),
                        // value
                        diner -> BeanUtil.fillBeanWithMap(diner, new ShortDinerInfo(), true)
                ));
        // 循環 VO 集合，根據用戶 ID 從 Map 中獲取用戶信息並設置至 VO 對象
        feedsVOS.forEach(feedsVO -> {
            feedsVO.setDinerInfo(dinerInfos.get(feedsVO.getFkDinerId()));
        });
        return feedsVOS;
    }

    /**
     * 變更 Feed
     *
     * @param followingDinerId 關注的好友 ID
     * @param accessToken      登錄用戶token
     * @param type             1 關注 0 取關
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFollowingFeed(Integer followingDinerId, String accessToken, int type) {
        // 請選擇關注的好友
        AssertUtil.isTrue(followingDinerId == null || followingDinerId < 1,
                "請選擇關注的好友");
        // 獲取登錄用戶信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取關注/取關的食客的所有 Feed
        List<Feeds> feedsList = feedsMapper.findByDinerId(followingDinerId);
        String key = RedisKeyConstant.following_feeds.getKey() + dinerInfo.getId();
        if (type == 0) {
            // 取關
            List<Integer> feedIds = feedsList.stream()
                    .map(feed -> feed.getId())
                    .collect(Collectors.toList());
            redisTemplate.opsForZSet().remove(key, feedIds.toArray(new Integer[]{}));
        } else {
            // 關注
            Set<ZSetOperations.TypedTuple> typedTuples =
                    feedsList.stream()
                            .map(feed -> new DefaultTypedTuple<>(feed.getId(), (double) feed.getUpdateDate().getTime()))
                            .collect(Collectors.toSet());
            redisTemplate.opsForZSet().add(key, typedTuples);
        }
    }

    /**
     * 刪除 Feed
     *
     * @param id
     * @param accessToken
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id, String accessToken) {
        // 請選擇要刪除的 Feed
        AssertUtil.isTrue(id == null || id < 1, "請選擇要刪除的Feed");
        // 獲取登錄用戶
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取 Feed 內容
        Feeds feeds = feedsMapper.findById(id);
        // 判斷 Feed 是否已經被刪除且只能刪除自己的 Feed
        AssertUtil.isTrue(feeds == null, "該Feed已被刪除");
        AssertUtil.isTrue(!feeds.getFkDinerId().equals(dinerInfo.getId()),
                "只能刪除自己的Feed");
        // 刪除
        int count = feedsMapper.delete(id);
        if (count == 0) {
            return;
        }
        // 將內容從粉絲的集合中刪除 -- 異步消息隊列優化
        // 先獲取我的粉絲
        List<Integer> followers = findFollowers(dinerInfo.getId());
        // 移除 Feed
        followers.forEach(follower -> {
            String key = RedisKeyConstant.following_feeds.getKey() + follower;
            redisTemplate.opsForZSet().remove(key, feeds.getId());
        });
    }

    /**
     * 添加 Feed
     *
     * @param feeds
     * @param accessToken
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(Feeds feeds, String accessToken) {
        // 校驗 Feed 內容不能為空，不能太長
        AssertUtil.isNotEmpty(feeds.getContent(), "請輸入內容");
        AssertUtil.isTrue(feeds.getContent().length() > 255, "輸入內容太多，請重新輸入");
        // 獲取登錄用戶信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // Feed 關聯用戶信息
        feeds.setFkDinerId(dinerInfo.getId());
        // 添加 Feed
        int count = feedsMapper.save(feeds);
        AssertUtil.isTrue(count == 0, "添加失敗");
        // 推送到粉絲的列表中 -- 後續這里應該采用異步消息隊列解決性能問題
        // 先獲取粉絲 id 集合
        List<Integer> followers = findFollowers(dinerInfo.getId());
        // 推送 Feed
        long now = System.currentTimeMillis();
        followers.forEach(follower -> {
            String key = RedisKeyConstant.following_feeds.getKey() + follower;
            redisTemplate.opsForZSet().add(key, feeds.getId(), now);
        });
    }

    /**
     * 獲取粉絲 id 集合
     *
     * @param dinerId
     * @return
     */
    private List<Integer> findFollowers(Integer dinerId) {
        String url = followServerName + "followers/" + dinerId;
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        List<Integer> followers = (List<Integer>) resultInfo.getData();
        return followers;
    }

    /**
     * 獲取登錄用戶信息
     *
     * @param accessToken
     * @return
     */
    private SignInDinerInfo loadSignInDinerInfo(String accessToken) {
        // 必須登錄
        AssertUtil.mustLogin(accessToken);
        String url = oauthServerName + "user/me?access_token={accessToken}";
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class, accessToken);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        SignInDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new SignInDinerInfo(), false);
        if (dinerInfo == null) {
            throw new ParameterException(ApiConstant.NO_LOGIN_CODE, ApiConstant.NO_LOGIN_MESSAGE);
        }
        return dinerInfo;
    }

}
