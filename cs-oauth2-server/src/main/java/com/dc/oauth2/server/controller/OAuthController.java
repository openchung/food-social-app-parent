package com.dc.oauth2.server.controller;

import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.utils.ResultInfoUtil;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Oauth2 控制器
 */
@RestController
@RequestMapping("oauth")
public class OAuthController {

    @Resource
    private TokenEndpoint tokenEndpoint;

    @Resource
    private HttpServletRequest request;

    @PostMapping("token")
    public ResultInfo postAccessToken(Principal principal, @RequestParam Map<String, String> parameters)
            throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

    /**
     * 自定義 Token 返回對象
     *
     * @param accessToken
     * @return
     */
    private ResultInfo custom(OAuth2AccessToken accessToken) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> data = new LinkedHashMap(token.getAdditionalInformation());
        data.put("accessToken", token.getValue());
        data.put("expireIn", token.getExpiresIn());
        data.put("scopes", token.getScope());
        if (token.getRefreshToken() != null) {
            data.put("refreshToken", token.getRefreshToken().getValue());
        }
        return ResultInfoUtil.buildSuccess(request.getServletPath(), data);
    }

}
