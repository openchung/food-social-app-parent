package com.dc.restaurants.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.pojo.Restaurant;
import com.dc.commons.model.pojo.Reviews;
import com.dc.commons.model.vo.ReviewsVO;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.restaurants.mapper.ReviewsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewsService {

    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Value("${service.name.cs-diners-server}")
    private String dinersServerName;
    @Resource
    private RestaurantService restaurantService;
    @Resource
    private ReviewsMapper reviewsMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RestTemplate restTemplate;
    private static final int NINE = 9;

    /**
     * 添加餐廳評論
     *
     * @param restaurantId 餐廳 ID
     * @param accessToken  登錄用戶 Token
     * @param content      評論內容
     * @param likeIt       是否喜歡
     */
    public void addReview(Integer restaurantId, String accessToken,
                          String content, int likeIt) {
        // 參數校驗
        AssertUtil.isTrue(restaurantId == null || restaurantId < 1,
                "請選擇要評論的餐廳");
        AssertUtil.isNotEmpty(content, "請輸入評論內容");
        AssertUtil.isTrue(content.length() > 800, "評論內容過長，請重新輸入");
        // 判斷餐廳是否存在
        Restaurant restaurant = restaurantService.findById(restaurantId);
        AssertUtil.isTrue(restaurant == null, "該餐廳不存在");
        // 獲取登錄用戶信息
        SignInDinerInfo signInDinerInfo = loadSignInDinerInfo(accessToken);
        // 插入數據庫
        Reviews reviews = new Reviews();
        reviews.setContent(content);
        reviews.setFkRestaurantId(restaurantId);
        reviews.setFkDinerId(signInDinerInfo.getId());
        // 這里需要後台操作處理餐廳數據(喜歡/不喜歡餐廳)做自增處理
        reviews.setLikeIt(likeIt);
        int count = reviewsMapper.saveReviews(reviews);
        if (count == 0) {
            return;
        }
        // 寫入餐廳最新評論至 Redis
        String key = RedisKeyConstant.restaurant_new_reviews.getKey() + restaurantId;
        redisTemplate.opsForList().leftPush(key, reviews);
    }

    /**
     * 獲取餐廳最新評論
     *
     * @param restaurantId
     * @param accessToken
     * @return
     */
    public List<ReviewsVO> findNewReviews(Integer restaurantId, String accessToken) {
        // 參數校驗
        AssertUtil.isTrue(restaurantId == null || restaurantId < 1,
                "請選擇要查看的餐廳");
        // 獲取 Key
        String key = RedisKeyConstant.restaurant_new_reviews.getKey() + restaurantId;
        // 從 Redis 取十條最新評論
        List<LinkedHashMap> reviews = redisTemplate.opsForList().range(key, 0, NINE);
        // 初始化 VO 集合
        List<ReviewsVO> reviewsVOS = Lists.newArrayList();
        // 初始化用戶 ID 集合
        List<Integer> dinerIds = Lists.newArrayList();
        // 循環處理評論集合
        reviews.forEach(review -> {
            ReviewsVO reviewsVO = BeanUtil.fillBeanWithMap(review, new ReviewsVO(), true);
            reviewsVOS.add(reviewsVO);
            dinerIds.add(reviewsVO.getFkDinerId());
        });
        // 獲取評論用戶信息
        ResultInfo resultInfo = restTemplate.getForObject(dinersServerName +
                        "findByIds?access_token=${accessToken}&ids={ids}",
                ResultInfo.class, accessToken, StrUtil.join(",", dinerIds));
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        List<LinkedHashMap> dinerInfoMaps = (List<LinkedHashMap>) resultInfo.getData();
        Map<Integer, ShortDinerInfo> dinerInfos = dinerInfoMaps.stream()
                .collect(Collectors.toMap(
                        // key
                        diner -> (int) diner.get("id"),
                        // value
                        diner -> BeanUtil.fillBeanWithMap(diner, new ShortDinerInfo(), true))
                );
        // 完善頭像昵稱信息
        reviewsVOS.forEach(review -> {
            ShortDinerInfo dinerInfo = dinerInfos.get(review.getFkDinerId());
            if (dinerInfo != null) {
                review.setDinerInfo(dinerInfo);
            }
        });
        // redis list 中只保留最新十條  ltrim
        return reviewsVOS;
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
