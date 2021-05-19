package com.dc.restaurants.service;

import cn.hutool.core.bean.BeanUtil;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.model.pojo.Restaurant;
import com.dc.commons.utils.AssertUtil;
import com.dc.restaurants.mapper.RestaurantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RestaurantService {

    @Resource
    public RestaurantMapper restaurantMapper;
    @Resource
    public RedisTemplate redisTemplate;

    /**
     * 根據餐廳 ID 查詢餐廳數據
     *
     * @param restaurantId
     * @return
     */
    public Restaurant findById(Integer restaurantId) {
        // 請選擇餐廳
        AssertUtil.isTrue(restaurantId == null, "請選擇餐廳查看");
        // 獲取 Key
        String key = RedisKeyConstant.restaurants.getKey() + restaurantId;
        // 獲取餐廳緩存
        LinkedHashMap restaurantMap = (LinkedHashMap) redisTemplate.opsForHash().entries(key);
        // 如果緩存不存在，查詢數據庫
        Restaurant restaurant = null;
        if (restaurantMap == null || restaurantMap.isEmpty()) {
            log.info("緩存失效了，查詢數據庫：{}", restaurantId);
            // 查詢數據庫
            restaurant = restaurantMapper.findById(restaurantId);
            if (restaurant != null) {
                // 更新緩存
                redisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(restaurant));
            } else {
                // 寫入緩存一個空數據，設置一個失效時間，60s
                redisTemplate.expire(key, 6000, TimeUnit.MILLISECONDS);
            }
        } else {
            restaurant = BeanUtil.fillBeanWithMap(restaurantMap,
                    new Restaurant(), false);
        }
        return restaurant;
    }

}