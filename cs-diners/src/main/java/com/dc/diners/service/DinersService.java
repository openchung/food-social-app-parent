package com.dc.diners.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.dto.DinersDTO;
import com.dc.commons.model.pojo.Diners;
import com.dc.commons.model.vo.ShortDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.commons.utils.ResultInfoUtil;
import com.dc.diners.config.OAuth2ClientConfiguration;
import com.dc.diners.domain.OAuthDinerInfo;
import com.dc.diners.mapper.DinersMapper;
import com.dc.diners.vo.LoginDinerInfo;
import com.google.inject.internal.cglib.proxy.$Dispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

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
    @Resource
    private DinersMapper dinersMapper;
    @Resource
    private SendVerifyCodeService sendVerifyCodeService;

    /**
     * 根據 ids 查詢食客信息
     *
     * @param ids 主鍵 id，多個以逗號分隔，逗號之間不用空格
     * @return
     */
    public List<ShortDinerInfo> findByIds(String ids) {
        AssertUtil.isNotEmpty(ids);
        String[] idArr = ids.split(",");
        List<ShortDinerInfo> dinerInfos = dinersMapper.findByIds(idArr);
        return dinerInfos;
    }


    /**
     * 使用者註冊
     * @param dinersDTO
     * @param path
     * @return
     */
    public ResultInfo register(DinersDTO dinersDTO, String path) {
        //參數非空驗證
        String username = dinersDTO.getUsername();
        AssertUtil.isNotEmpty(username, "請輸入使用者名稱");
        String password = dinersDTO.getPassword();
        AssertUtil.isNotEmpty(password, "請輸入密碼");
        String phone = dinersDTO.getPhone();
        AssertUtil.isNotEmpty(phone, "請輸入手機號碼");
        String verifyCode = dinersDTO.getVerifyCode();
        AssertUtil.isNotEmpty(verifyCode, "請輸入驗證碼");
        // 取得此手機目前驗證碼
        String code = sendVerifyCodeService.getCodeByPhone(phone);
        // 驗證交易驗證碼是否已過期
        AssertUtil.isNotEmpty(code, "驗證碼已過期，請重新發送!");
        // 驗證碼一致性驗證
        AssertUtil.isTrue(!dinersDTO.getVerifyCode().equals(code), "驗證碼不一致，請重新輸入");

        // 驗證用戶端是否已註冊
        Diners diners = dinersMapper.selectByUsername(username.trim());
        AssertUtil.isTrue(diners != null, "此帳號已註冊，請重新輸入");

        // 註冊
        // 密碼加密
        dinersDTO.setPassword(DigestUtil.md5Hex(password.trim()));
        dinersMapper.save(dinersDTO);
        //自動登入
        return signIn(username.trim(), password.trim(), path);

    }


    /**
     * 驗證手機號是否已註冊
     */
    public  void checkPhoneIsRegistered(String phone) {
        AssertUtil.isNotEmpty(phone, "手機號不能為空");
        Diners diners = dinersMapper.selectByPhone(phone);
        AssertUtil.isTrue(diners == null, "該手機號未註冊過");
        AssertUtil.isTrue(diners.getIsValid() == 0, "該使用者已鎖定，請先解鎖!");
    }
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
