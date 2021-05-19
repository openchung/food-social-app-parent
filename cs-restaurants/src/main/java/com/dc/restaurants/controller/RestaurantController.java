package com.dc.restaurants.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.pojo.Restaurant;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.restaurants.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class RestaurantController {

    @Resource
    private RestaurantService restaurantService;
    @Resource
    private HttpServletRequest request;

    /**
     * 根據餐廳 ID 查詢餐廳數據
     *
     * @param restaurantId
     * @return
     */
    @GetMapping("{restaurantId}")
    public ResultInfo<Restaurant> findById(@PathVariable Integer restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), restaurant);
    }

}