package com.dc.diners.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.vo.NearMeDinerVO;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NearMeService {

    @Resource
    private DinersService dinersService;
    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 更新食客坐標
     *
     * @param accessToken 登錄用戶 token
     * @param lon         經度
     * @param lat         緯度
     */
    public void updateDinerLocation(String accessToken, Float lon, Float lat) {
        // 參數校驗
        AssertUtil.isTrue(lon == null, "獲取經度失敗");
        AssertUtil.isTrue(lat == null, "獲取緯度失敗");
        // 獲取登錄用戶信息
        SignInDinerInfo signInDinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取 key diner:location
        String key = RedisKeyConstant.diner_location.getKey();
        // 將用戶地理位置信息存入 Redis
        RedisGeoCommands.GeoLocation geoLocation = new RedisGeoCommands
                .GeoLocation(signInDinerInfo.getId(), new Point(lon, lat));
        redisTemplate.opsForGeo().add(key, geoLocation);
    }

    /**
     * 獲取附近的人
     *
     * @param accessToken 用戶登錄 token
     * @param radius      半徑，默認 1000m
     * @param lon         經度
     * @param lat         緯度
     * @return
     */
    public List<NearMeDinerVO> findNearMe(String accessToken,
                                          Integer radius,
                                          Float lon, Float lat) {
        // 獲取登錄用戶信息
        SignInDinerInfo signInDinerInfo = loadSignInDinerInfo(accessToken);
        // 食客 ID
        Integer dinerId = signInDinerInfo.getId();
        // 處理半徑，默認 1000m
        if (radius == null) {
            radius = 1000;
        }
        // 獲取 key
        String key = RedisKeyConstant.diner_location.getKey();
        // 獲取用戶經緯度
        Point point = null;
        if (lon == null || lat == null) {
            // 如果經緯度沒傳，那麽從 Redis 中獲取
            List<Point> points = redisTemplate.opsForGeo().position(key, dinerId);
            AssertUtil.isTrue(points == null || points.isEmpty(),
                    "獲取經緯度失敗");
            point = points.get(0);
        } else {
            point = new Point(lon, lat);
        }
        // 初始化距離對象，單位 m
        Distance distance = new Distance(radius,
                RedisGeoCommands.DistanceUnit.METERS);
        // 初始化 Geo 命令參數對象
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        // 附近的人限制 20，包含距離，按由近到遠排序
        args.limit(20).includeDistance().sortAscending();
        // 以用戶經緯度為圓心，範圍 1000m
        Circle circle = new Circle(point, distance);
        // 獲取附近的人 GeoLocation 信息
        GeoResults<RedisGeoCommands.GeoLocation> geoResult =
                redisTemplate.opsForGeo().radius(key, circle, args);
        // 構建有序 Map
        Map<Integer, NearMeDinerVO> nearMeDinerVOMap = Maps.newLinkedHashMap();
        // 完善用戶頭像昵稱信息
        geoResult.forEach(result -> {
            RedisGeoCommands.GeoLocation<Integer> geoLocation = result.getContent();
            // 初始化 Vo 對象
            NearMeDinerVO nearMeDinerVO = new NearMeDinerVO();
            nearMeDinerVO.setId(geoLocation.getName());
            // 獲取距離
            Double dist = result.getDistance().getValue();
            // 四舍五入精確到小數點後 1 位，方便客戶端顯示
            String distanceStr = NumberUtil.round(dist, 1).toString() + "m";
            nearMeDinerVO.setDistance(distanceStr);
            nearMeDinerVOMap.put(geoLocation.getName(), nearMeDinerVO);
        });
        // 獲取附近的人的信息（根據 Diner 服務接口獲取）
        Integer[] dinerIds = nearMeDinerVOMap.keySet().toArray(new Integer[]{});
        List<ShortDinerInfo> shortDinerInfos = dinersService.findByIds(StrUtil.join(",", dinerIds));
        // 完善昵稱頭像信息
        shortDinerInfos.forEach(shortDinerInfo -> {
            NearMeDinerVO nearMeDinerVO = nearMeDinerVOMap.get(shortDinerInfo.getId());
            nearMeDinerVO.setNickname(shortDinerInfo.getNickname());
            nearMeDinerVO.setAvatarUrl(shortDinerInfo.getAvatarUrl());
        });
        return Lists.newArrayList(nearMeDinerVOMap.values());
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
