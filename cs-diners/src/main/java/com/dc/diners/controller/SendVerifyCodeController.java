package com.dc.diners.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.diners.service.SendVerifyCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 發送驗證碼控制層
 */
@RestController
public class SendVerifyCodeController {

    @Resource
    private SendVerifyCodeService sendVerifyCodeService;

    @Resource
    private HttpServletRequest request;

    /**
     * 發送驗證碼
     * @param phone
     * @return
     */
    @GetMapping("send")
    public ResultInfo send(String phone) {
        sendVerifyCodeService.send(phone);
        return ResultInfoUtil.buildSuccess("發送成功", request.getServletPath());
    }

}
