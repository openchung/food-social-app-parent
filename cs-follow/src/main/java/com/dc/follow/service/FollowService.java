package com.dc.follow.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.pojo.Follow;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.follow.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 關注/取關業務邏輯層
 */
@Service
public class FollowService {

    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Value("${service.name.cs-diners-server}")
    private String dinersServerName;
    @Value("${service.name.cs-feeds-server}")
    private String feedsServerName;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private FollowMapper followMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 獲取粉絲列表
     *
     * @param dinerId
     * @return
     */
    public Set<Integer> findFollowers(Integer dinerId) {
        AssertUtil.isNotNull(dinerId, "請選擇要查看的用戶");
        Set<Integer> followers = redisTemplate.opsForSet()
                .members(RedisKeyConstant.followers.getKey() + dinerId);
        return followers;
    }

    /**
     * 共同關注列表
     *
     * @param dinerId
     * @param accessToken
     * @param path
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultInfo findCommonsFriends(Integer dinerId, String accessToken, String path) {
        // 是否選擇了查看對象
        AssertUtil.isTrue(dinerId == null || dinerId < 1,
                "請選擇要查看的人");
        // 獲取登錄用戶信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取登錄用戶的關注信息
        String loginDinerKey = RedisKeyConstant.following.getKey() + dinerInfo.getId();
        // 獲取登錄用戶查看對象的關注信息
        String dinerKey = RedisKeyConstant.following.getKey() + dinerId;
        // 計算交集
        Set<Integer> dinerIds = redisTemplate.opsForSet().intersect(loginDinerKey, dinerKey);
        // 沒有
        if (dinerIds == null || dinerIds.isEmpty()) {
            return ResultInfoUtil.buildSuccess(path, new ArrayList<ShortDinerInfo>());
        }
        // 調用食客服務根據 ids 查詢食客信息
        ResultInfo resultInfo = restTemplate.getForObject(dinersServerName + "findByIds?access_token={accessToken}&ids={ids}",
                ResultInfo.class, accessToken, StrUtil.join(",", dinerIds));
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            resultInfo.setPath(path);
            return resultInfo;
        }
        // 處理結果集
        List<LinkedHashMap> dinnerInfoMaps = (ArrayList) resultInfo.getData();
        List<ShortDinerInfo> dinerInfos = dinnerInfoMaps.stream()
                .map(diner -> BeanUtil.fillBeanWithMap(diner, new ShortDinerInfo(), true))
                .collect(Collectors.toList());

        return ResultInfoUtil.buildSuccess(path, dinerInfos);
    }

    /**
     * 關注/取關
     *
     * @param followDinerId 關注的食客ID
     * @param isFolowed    是否關注 1=關注 0=取關
     * @param accessToken   登錄用戶token
     * @param path          訪問地址
     * @return
     */
    public ResultInfo follow(Integer followDinerId, int isFolowed,
                             String accessToken, String path) {
        // 是否選擇了關注對象
        AssertUtil.isTrue(followDinerId == null || followDinerId < 1,
                "請選擇要關注的人");
        // 獲取登錄用戶信息 (封裝方法)
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取當前登錄用戶與需要關注用戶的關注信息
        Follow follow = followMapper.selectFollow(dinerInfo.getId(), followDinerId);

        // 如果沒有關注信息，且要進行關注操作 -- 添加關注
        if (follow == null && isFolowed == 1) {
            // 添加關注信息
            int count = followMapper.save(dinerInfo.getId(), followDinerId);
            // 添加關注列表到 Redis
            if (count == 1) {
                addToRedisSet(dinerInfo.getId(), followDinerId);
                // 保存 Feed
                sendSaveOrRemoveFeed(followDinerId, accessToken, 1);
            }
            return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE,
                    "關注成功", path, "關注成功");
        }

        // 如果有關注信息，且目前處於關注狀態，且要進行取關操作 -- 取關關注
        if (follow != null && follow.getIsValid() == 1 && isFolowed == 0) {
            // 取關
            int count = followMapper.update(follow.getId(), isFolowed);
            // 移除 Redis 關注列表
            if (count == 1) {
                removeFromRedisSet(dinerInfo.getId(), followDinerId);
                // 移除 Feed
                sendSaveOrRemoveFeed(followDinerId, accessToken, 0);
            }
            return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE,
                    "成功取關", path, "成功取關");
        }

        // 如果有關注信息，且目前處於取關狀態，且要進行關注操作 -- 重新關注
        if (follow != null && follow.getIsValid() == 0 && isFolowed == 1) {
            // 重新關注
            int count = followMapper.update(follow.getId(), isFolowed);
            // 添加關注列表到 Redis
            if (count == 1) {
                addToRedisSet(dinerInfo.getId(), followDinerId);
                // 添加 Feed
                sendSaveOrRemoveFeed(followDinerId, accessToken, 1);
            }
            return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE,
                    "關注成功", path, "關注成功");
        }

        return ResultInfoUtil.buildSuccess(path, "操作成功");
    }

    /**
     * 发送請求添加或者移除關注人的Feed列表
     *
     * @param followDinerId 關注好友的ID
     * @param accessToken   當前登錄用戶token
     * @param type          0=取關 1=關注
     */
    private void sendSaveOrRemoveFeed(Integer followDinerId, String accessToken, int type) {
        String feedsUpdateUrl = feedsServerName + "updateFollowingFeeds/"
                + followDinerId + "?access_token=" + accessToken;
        // 構建請求頭
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 構建請求體（請求參數）
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("type", type);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(feedsUpdateUrl, entity, ResultInfo.class);
    }

    /**
     * 添加關注列表到 Redis
     *
     * @param dinerId
     * @param followDinerId
     */
    private void addToRedisSet(Integer dinerId, Integer followDinerId) {
        redisTemplate.opsForSet().add(RedisKeyConstant.following.getKey() + dinerId, followDinerId);
        redisTemplate.opsForSet().add(RedisKeyConstant.followers.getKey() + followDinerId, dinerId);
    }

    /**
     * 移除 Redis 關注列表
     *
     * @param dinerId
     * @param followDinerId
     */
    private void removeFromRedisSet(Integer dinerId, Integer followDinerId) {
        redisTemplate.opsForSet().remove(RedisKeyConstant.following.getKey() + dinerId, followDinerId);
        redisTemplate.opsForSet().remove(RedisKeyConstant.followers.getKey() + followDinerId, dinerId);
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
