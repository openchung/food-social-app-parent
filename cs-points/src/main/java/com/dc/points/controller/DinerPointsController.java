package com.dc.points.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.vo.DinerPointsRankVO;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.points.service.DinerPointsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 積分控制層
 */
@RestController
public class DinerPointsController {

    @Resource
    private DinerPointsService dinerPointsService;
    @Resource
    private HttpServletRequest request;

    /**
     * 添加積分
     *
     * @param dinerId 食客ID
     * @param points  積分
     * @param types   類型 0=簽到，1=關注好友，2=添加Feed，3=添加商戶評論
     * @return
     */
    @PostMapping
    public ResultInfo<Integer> addPoints(@RequestParam(required = false) Integer dinerId,
                                         @RequestParam(required = false) Integer points,
                                         @RequestParam(required = false) Integer types) {
        dinerPointsService.addPoints(dinerId, points, types);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), points);
    }

    /**
     * 查詢前 20 積分排行榜，同時顯示用戶排名 -- Redis
     *
     * @param access_token
     * @return
     */
    @GetMapping("redis")
    public ResultInfo findDinerPointsRankFromRedis(String access_token) {
        List<DinerPointsRankVO> ranks = dinerPointsService.findDinerPointRankFromRedis(access_token);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), ranks);
    }

    /**
     * 查詢前 20 積分排行榜，同時顯示用戶排名 -- MySQL
     *
     * @param access_token
     * @return
     */
    @GetMapping
    public ResultInfo findDinerPointsRank(String access_token) {
        List<DinerPointsRankVO> ranks = dinerPointsService.findDinerPointRank(access_token);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), ranks);
    }

}