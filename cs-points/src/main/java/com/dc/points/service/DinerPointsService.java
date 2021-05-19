package com.dc.points.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.pojo.DinerPoints;
import com.dc.commons.model.vo.DinerPointsRankVO;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.points.mapper.DinerPointsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 積分業務邏輯層
 */
@Service
public class DinerPointsService {

    @Resource
    private DinerPointsMapper dinerPointsMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Value("${service.name.cs-diners-server}")
    private String dinersServerName;
    // 排行榜 TOPN
    private static final int TOPN = 20;

    /**
     * 添加積分
     *
     * @param dinerId 食客ID
     * @param points  積分
     * @param types   類型 0=簽到，1=關注好友，2=添加Feed，3=添加商戶評論
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Integer dinerId, Integer points, Integer types) {
        // 基本參數校驗
        AssertUtil.isTrue(dinerId == null || dinerId < 1, "食客不能為空");
        AssertUtil.isTrue(points == null || points < 1, "積分不能為空");
        AssertUtil.isTrue(types == null, "請選擇對應的積分類型");

        // 插入數據庫
        DinerPoints dinerPoints = new DinerPoints();
        dinerPoints.setFkDinerId(dinerId);
        dinerPoints.setPoints(points);
        dinerPoints.setTypes(types);
        dinerPointsMapper.save(dinerPoints);

        // 將積分保存到 Redis
        redisTemplate.opsForZSet().incrementScore(
                RedisKeyConstant.diner_points.getKey(), dinerId, points);
    }

    /**
     * 查詢前 20 積分排行榜，並顯示個人排名 -- Redis
     *
     * @param accessToken
     * @return
     */
    public List<DinerPointsRankVO> findDinerPointRankFromRedis(String accessToken) {
        // 獲取登錄用戶信息
        SignInDinerInfo signInDinerInfo = loadSignInDinerInfo(accessToken);
        // 統計積分排行榜
        Set<ZSetOperations.TypedTuple<Integer>> rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(
                RedisKeyConstant.diner_points.getKey(), 0, 19);
        if (rangeWithScores == null || rangeWithScores.isEmpty()) {
            return Lists.newArrayList();
        }
        // 初始化食客 ID 集合
        List<Integer> rankDinerIds = Lists.newArrayList();
        // 根據 key：食客 ID value：積分信息 構建一個 Map
        Map<Integer, DinerPointsRankVO> ranksMap = new LinkedHashMap<>();
        // 初始化排名
        int rank = 1;
        // 循環處理排行榜，添加排名信息
        for (ZSetOperations.TypedTuple<Integer> rangeWithScore : rangeWithScores) {
            // 食客ID
            Integer dinerId = rangeWithScore.getValue();
            // 積分
            int points = rangeWithScore.getScore().intValue();
            // 將食客 ID 添加至食客 ID 集合
            rankDinerIds.add(dinerId);
            DinerPointsRankVO dinerPointsRankVO = new DinerPointsRankVO();
            dinerPointsRankVO.setId(dinerId);
            dinerPointsRankVO.setRanks(rank);
            dinerPointsRankVO.setTotal(points);
            // 將 VO 對象添加至 Map 中
            ranksMap.put(dinerId, dinerPointsRankVO);
            // 排名 +1
            rank++;
        }

        // 獲取 Diners 用戶信息
        ResultInfo resultInfo = restTemplate.getForObject(dinersServerName +
                        "findByIds?access_token=${accessToken}&ids={ids}",
                ResultInfo.class, accessToken, StrUtil.join(",", rankDinerIds));
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        List<LinkedHashMap> dinerInfoMaps = (List<LinkedHashMap>) resultInfo.getData();
        // 完善食客昵稱和頭像
        for (LinkedHashMap dinerInfoMap : dinerInfoMaps) {
            ShortDinerInfo shortDinerInfo = BeanUtil.fillBeanWithMap(dinerInfoMap,
                    new ShortDinerInfo(), false);
            DinerPointsRankVO rankVO = ranksMap.get(shortDinerInfo.getId());
            rankVO.setNickname(shortDinerInfo.getNickname());
            rankVO.setAvatarUrl(shortDinerInfo.getAvatarUrl());
        }

        // 判斷個人是否在 ranks 中，如果在，添加標記直接返回
        if (ranksMap.containsKey(signInDinerInfo.getId())) {
            DinerPointsRankVO rankVO = ranksMap.get(signInDinerInfo.getId());
            rankVO.setIsMe(1);
            return Lists.newArrayList(ranksMap.values());
        }

        // 如果不在 ranks 中，獲取個人排名追加在最後
        // 獲取排名
        Long myRank = redisTemplate.opsForZSet().reverseRank(
                RedisKeyConstant.diner_points.getKey(), signInDinerInfo.getId());
        if (myRank != null) {
            DinerPointsRankVO me = new DinerPointsRankVO();
            BeanUtils.copyProperties(signInDinerInfo, me);
            me.setRanks(myRank.intValue() + 1);// 排名從 0 開始
            me.setIsMe(1);
            // 獲取積分
            Double points = redisTemplate.opsForZSet().score(RedisKeyConstant.diner_points.getKey(),
                    signInDinerInfo.getId());
            me.setTotal(points.intValue());
            ranksMap.put(signInDinerInfo.getId(), me);
        }
        return Lists.newArrayList(ranksMap.values());
    }

    /**
     * 查詢前 20 積分排行榜，並顯示個人排名 -- MySQL
     *
     * @param accessToken
     * @return
     */
    public List<DinerPointsRankVO> findDinerPointRank(String accessToken) {
        // 獲取登錄用戶信息
        SignInDinerInfo signInDinerInfo = loadSignInDinerInfo(accessToken);
        // 統計積分排行榜
        List<DinerPointsRankVO> ranks = dinerPointsMapper.findTopN(TOPN);
        if (ranks == null || ranks.isEmpty()) {
            return Lists.newArrayList();
        }
        // 根據 key：食客 ID value：積分信息 構建一個 Map
        Map<Integer, DinerPointsRankVO> ranksMap = new LinkedHashMap<>();
        for (int i = 0; i < ranks.size(); i++) {
            ranksMap.put(ranks.get(i).getId(), ranks.get(i));
        }
        // 判斷個人是否在 ranks 中，如果在，添加標記直接返回
        if (ranksMap.containsKey(signInDinerInfo.getId())) {
            DinerPointsRankVO myRank = ranksMap.get(signInDinerInfo.getId());
            myRank.setIsMe(1);
            return Lists.newArrayList(ranksMap.values());
        }
        // 如果不在 ranks 中，獲取個人排名追加在最後
        DinerPointsRankVO myRank = dinerPointsMapper.findDinerRank(signInDinerInfo.getId());
        myRank.setIsMe(1);
        ranks.add(myRank);
        return ranks;
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