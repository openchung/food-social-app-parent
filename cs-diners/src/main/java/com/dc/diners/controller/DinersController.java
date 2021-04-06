package com.dc.diners.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.diners.service.DinersService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "食客相關接口")
public class DinersController {
    @Resource
    private DinersService dinersService;

    @Resource
    private HttpServletRequest request;

    /**
     * 登入
     *
     * @param account
     * @param password
     * @return
     */
    @GetMapping("signin")
    public ResultInfo signIn(String account, String password) {
        return  dinersService.signIn(account, password, request.getServletPath());
    }
}
