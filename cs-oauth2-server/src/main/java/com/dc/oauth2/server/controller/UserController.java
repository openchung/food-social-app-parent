package com.dc.oauth2.server.controller;

import cn.hutool.core.bean.BeanUtil;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.domain.SignInIdentity;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.ResultInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisTokenStore redisTokenStore;

    @GetMapping("user/me")
    public ResultInfo getCurrentUser(Authentication authentication) {
        //取得登入後，使用者的相關資訊，然後設置
        SignInIdentity signInIdentity = (SignInIdentity) authentication.getPrincipal();
        //轉為前端可用的視圖對象
        SignInDinerInfo dinerInfo = new SignInDinerInfo();
        BeanUtil.copyProperties(signInIdentity, dinerInfo);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), dinerInfo);
    }
    //http://localhost:8082/user/me?access_token=4da4fed5-f9ae-4888-8017-ea41e73dab32

    /**
     * 安全退出
     * @param access_token
     * @param authorization
     * @return
     */
    @GetMapping("user/logout")
    public ResultInfo logout(String access_token, String authorization) {
        //判斷 access_token是否為空，為空將 authorization 賦值給 access_token
        if(StringUtils.isBlank(access_token)) {
            access_token = authorization;
        }
        //判斷 authorization 是否為空
        if(StringUtils.isBlank(access_token)) {
            return ResultInfoUtil.buildSuccess(request.getServletPath(), "退出成功");
        }
        //判斷 bearer token 是否為空
        if(access_token.toLowerCase().contains("bearer ".toLowerCase())) {
            access_token = access_token.toLowerCase().replace("bearer ","");
        }
        //清除 redis token 訊息
        OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(access_token);
        if(oAuth2AccessToken!=null){
            redisTokenStore.removeAccessToken(oAuth2AccessToken);
            OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
            redisTokenStore.removeRefreshToken(refreshToken);
        }
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "退出成功");
    }
}
