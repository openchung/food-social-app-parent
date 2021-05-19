package com.dc.diners.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.diners.service.SignService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 簽到控制層
 */
@RestController
@RequestMapping("sign")
public class SignController {

    @Resource
    private SignService signService;
    @Resource
    private HttpServletRequest request;

    /**
     * 獲取用戶簽到情況 默認當月
     *
     * @param access_token
     * @param dateStr
     * @return
     */
    @GetMapping
    public ResultInfo getSignInfo(String access_token, String dateStr) {
        Map<String, Boolean> map = signService.getSignInfo(access_token, dateStr);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), map);
    }

    /**
     * 獲取簽到次數 默認當月
     *
     * @param access_token
     * @param date
     * @return
     */
    @GetMapping("count")
    public ResultInfo getSignCount(String access_token, String date) {
        Long count = signService.getSignCount(access_token, date);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), count);
    }

    /**
     * 簽到，可以補簽
     *
     * @param access_token
     * @param date
     * @return
     */
    @PostMapping
    public ResultInfo sign(String access_token,
                           @RequestParam(required = false) String date) {
        int count = signService.doSign(access_token, date);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), count);
    }

}