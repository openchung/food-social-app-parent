package com.dc.diners.service;

import cn.hutool.core.bean.BeanUtil;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.diners.config.OAuth2ClientConfiguration;
import com.dc.diners.domain.OAuthDinerInfo;
import com.dc.diners.vo.LoginDinerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 *  食客服務業務邏輯層
 * */
@Service
public class DinersService {
    @Resource
    private RestTemplate restTemplate;
    @Value("${service.name.cs-oauth-server}")
    private String pauthServerName;
    @Resource
    private OAuth2ClientConfiguration oAuth2ClientConfiguration;

    /**
     * 登入
     * @param account   帳號、使用者名稱、手機或Email
     * @param password  密碼
     * @param path      請求路徑
     * @return
     */
    public ResultInfo signIn(String account, String password, String path) {
        // 參數驗證
        AssertUtil.isNotEmpty(account, "請輸入登入帳號");
        AssertUtil.isNotEmpty(password, "請輸入登入密碼");

        // 建立請求頭
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 建立請求體(請求參數)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("username", account);
        body.add("password", password);
        body.setAll(BeanUtil.beanToMap(oAuth2ClientConfiguration));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        // 設置Authorization
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor(oAuth2ClientConfiguration.getClientId(), oAuth2ClientConfiguration.getSecret()));
        // 發送請求
        ResponseEntity<ResultInfo> result = restTemplate.postForEntity(pauthServerName + "oauth/token", entity, ResultInfo.class);
        // 處理返回結果
        AssertUtil.isTrue(result.getStatusCode() != HttpStatus.OK, "登入失敗");
        ResultInfo resultInfo = result.getBody();
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            // 登入失敗
            resultInfo.setData(resultInfo.getMessage());
            return resultInfo;
        }
        // 這裡的 Data 是一個 LinkedHashMap 轉成了域對象 OAuthDinerInfo
        OAuthDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap)resultInfo.getData(), new OAuthDinerInfo(), false);
        // 根據業務需求返回視圖對象
        LoginDinerInfo loginDinerInfo = new LoginDinerInfo();
        loginDinerInfo.setToken(dinerInfo.getAccessToken());
        loginDinerInfo.setAvatarUrl(dinerInfo.getAvatarUrl());
        loginDinerInfo.setNickname(dinerInfo.getNickname());
        return ResultInfoUtil.buildSuccess(path, loginDinerInfo);
    }
}
