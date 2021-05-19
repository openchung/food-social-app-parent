package com.dc.diners.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.exception.ParameterException;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.model.vo.SignInDinerInfo;
import com.dc.commons.utils.AssertUtil;
import com.dc.commons.constant.PointTypesConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SignService {
    @Value("${service.name.cs-oauth-server}")
    private String oauthServerName;
    @Value("${service.name.cs-points-server}")
    private String pointsServerName;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 獲取當月簽到情況
     *
     * @param accessToken
     * @param dateStr
     * @return
     */
    public Map<String, Boolean> getSignInfo(String accessToken, String dateStr) {
        // 獲取登錄用戶信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取日期
        Date date = getDate(dateStr);
        // 構建 Key
        String signKey = buildSignKey(dinerInfo.getId(), date);
        // 構建一個自動排序的 Map
        Map<String, Boolean> signInfo = new TreeMap<>();
        // 獲取某月的總天數（考慮閏年）
        int dayOfMonth = DateUtil.lengthOfMonth(DateUtil.month(date) + 1,
                DateUtil.isLeapYear(DateUtil.year(date)));
        // bitfield user:sign:5:202011 u30 0
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);
        List<Long> list = redisTemplate.opsForValue().bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return signInfo;
        }
        long v = list.get(0) == null ? 0 : list.get(0);
        // 從低位到高位進行遍歷，為 0 表示未簽到，為 1 表示已簽到
        for (int i = dayOfMonth; i > 0; i--) {
            /*
                簽到：  yyyy-MM-01 true
                未簽到：yyyy-MM-01 false
             */
            LocalDateTime localDateTime = LocalDateTimeUtil.of(date).withDayOfMonth(i);
            boolean flag = v >> 1 << 1 != v;
            signInfo.put(DateUtil.format(localDateTime, "yyyy-MM-dd"), flag);
            v >>= 1;
        }
        return signInfo;
    }

    /**
     * 獲取用戶簽到次數
     *
     * @param accessToken
     * @param dateStr
     * @return
     */
    public long getSignCount(String accessToken, String dateStr) {
        // 獲取登錄用戶信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取日期
        Date date = getDate(dateStr);
        // 構建 Key
        String signKey = buildSignKey(dinerInfo.getId(), date);
        // e.g. BITCOUNT user:sign:5:202011
        return (Long) redisTemplate.execute(
                (RedisCallback<Long>) con -> con.bitCount(signKey.getBytes())
        );
    }

    /**
     * 用戶簽到
     *
     * @param accessToken
     * @param dateStr
     * @return
     */
    public int doSign(String accessToken, String dateStr) {
        // 獲取登錄用戶信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 獲取日期
        Date date = getDate(dateStr);
        // 獲取日期對應的天數，多少號
        int offset = DateUtil.dayOfMonth(date) - 1; // 從 0 開始
        // 構建 Key user:sign:5:yyyyMM
        String signKey = buildSignKey(dinerInfo.getId(), date);
        // 查看是否已簽到
        boolean isSigned = redisTemplate.opsForValue().getBit(signKey, offset);
        AssertUtil.isTrue(isSigned, "當前日期已完成簽到，無需再簽");
        // 簽到
        redisTemplate.opsForValue().setBit(signKey, offset, true);
        // 統計連續簽到的次數
        int count = getContinuousSignCount(dinerInfo.getId(), date);
        // 添加簽到積分並返回
        int points = addPoints(count, dinerInfo.getId());
        return points;
    }

    /**
     * 統計連續簽到的次數
     *
     * @param dinerId
     * @param date
     * @return
     */
    private int getContinuousSignCount(Integer dinerId, Date date) {
        // 獲取日期對應的天數，多少號，假設是 30
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 構建 Key
        String signKey = buildSignKey(dinerId, date);
        // bitfield user:sgin:5:202011 u30 0
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);
        List<Long> list = redisTemplate.opsForValue().bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int signCount = 0;
        long v = list.get(0) == null ? 0 : list.get(0);
        for (int i = dayOfMonth; i > 0; i--) {// i 表示位移操作次數
            // 右移再左移，如果等於自己說明最低位是 0，表示未簽到
            if (v >> 1 << 1 == v) {
                // 低位 0 且非當天說明連續簽到中斷了
                if (i != dayOfMonth) break;
            } else {
                signCount++;
            }
            // 右移一位並重新賦值，相當於把最低位丟棄一位
            v >>= 1;
        }
        return signCount;
    }

    /**
     * 構建 Key -- user:sign:5:yyyyMM
     *
     * @param dinerId
     * @param date
     * @return
     */
    private String buildSignKey(Integer dinerId, Date date) {
        return String.format("user:sign:%d:%s", dinerId,
                DateUtil.format(date, "yyyyMM"));
    }

    /**
     * 獲取日期
     *
     * @param dateStr
     * @return
     */
    private Date getDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return new Date();
        }
        try {
            return DateUtil.parseDate(dateStr);
        } catch (Exception e) {
            throw new ParameterException("請傳入yyyy-MM-dd的日期格式");
        }
    }

    /**
     * 獲取登錄用戶信息
     *
     * @param accessToken
     * @return
     */
    private SignInDinerInfo loadSignInDinerInfo(String accessToken) {
        // 必須登錄
        AssertUtil.mustLogin(accessToken);
        String url = oauthServerName + "user/me?access_token={accessToken}";
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class, accessToken);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        SignInDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new SignInDinerInfo(), false);
        if (dinerInfo == null) {
            throw new ParameterException(ApiConstant.NO_LOGIN_CODE, ApiConstant.NO_LOGIN_MESSAGE);
        }
        return dinerInfo;
    }

    /**
     * 添加用戶積分
     *
     * @param count         連續簽到次數
     * @param signInDinerId 登錄用戶id
     * @return 獲取的積分
     */
    private int addPoints(int count, Integer signInDinerId) {
        // 簽到1天送10積分，連續簽到2天送20積分，3天送30積分，4天以上均送50積分
        int points = 10;
        if (count == 2) {
            points = 20;
        } else if (count == 3) {
            points = 30;
        } else if (count >= 4) {
            points = 50;
        }
        // 調用積分接口添加積分
        // 構建請求頭
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 構建請求體（請求參數）
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dinerId", signInDinerId);
        body.add("points", points);
        body.add("types", PointTypesConstant.sign.getType());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        // 发送請求
        ResponseEntity<ResultInfo> result = restTemplate.postForEntity(pointsServerName,
                entity, ResultInfo.class);
        AssertUtil.isTrue(result.getStatusCode() != HttpStatus.OK, "登錄失敗！");
        ResultInfo resultInfo = result.getBody();
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            // 失敗了, 事物要進行回滾
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        return points;
    }
}
