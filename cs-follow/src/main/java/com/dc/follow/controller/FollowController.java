package com.dc.follow.controller;


import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.follow.service.FollowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 關注/取關控制層
 */
@RestController
public class FollowController {

    @Resource
    private FollowService followService;
    @Resource
    private HttpServletRequest request;

    /**
     * 獲取粉絲列表
     *
     * @param dinerId
     * @return
     */
    @GetMapping("followers/{dinerId}")
    public ResultInfo findFollowers(@PathVariable Integer dinerId) {
        return ResultInfoUtil.buildSuccess(request.getServletPath(),
                followService.findFollowers(dinerId));
    }

    /**
     * 共同關注列表
     *
     * @param dinerId
     * @param access_token
     * @return
     */
    @GetMapping("commons/{dinerId}")
    public ResultInfo findCommonsFriends(@PathVariable Integer dinerId,
                                         String access_token) {
        return followService.findCommonsFriends(dinerId, access_token, request.getServletPath());
    }


    /**
     * 關注/取關
     *
     * @param followDinerId 關注的食客ID
     * @param isFollowed    是否關注 1=關注 0=取消
     * @param access_token  登錄用戶token
     * @return
     */
    @PostMapping("/{followDinerId}")
    public ResultInfo follow(@PathVariable Integer followDinerId,
                             @RequestParam int isFollowed,
                             String access_token) {
        ResultInfo resultInfo = followService.follow(followDinerId,
                isFollowed, access_token, request.getServletPath());
        return resultInfo;
    }

}
