package com.dc.diners.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.dc.commons.constant.RedisKeyConstant;
import com.dc.commons.utils.AssertUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 發送驗證碼業務邏輯層
 * */
@Service
public class SendVerifyCodeService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 發送驗證碼
     * @param phone
     * */
    public void send(String phone) {
        // 檢查非空
        AssertUtil.isNotEmpty(phone, "手機號不能為空");
        // 根據手機號檢查是否已生成驗證碼，已生成直接返回
        if (!checkCodeIsExpired(phone)) {
            return;
        }
        // 生成6位驗證碼
        String code = RandomUtil.randomNumbers(6);
        // 呼叫短信服務發送通知信

        // 發送成功，將 code 保存至 Redis，設置失效時間 60s
        String key = RedisKeyConstant.verify_code.getKey() + phone;
        redisTemplate.opsForValue().set(key, code,60, TimeUnit.SECONDS);
    }

    /**
     * 根據手機號檢查是否已生成驗證碼
     * @param phone
     * @return
     */
    private boolean checkCodeIsExpired(String phone) {
        String key = RedisKeyConstant.verify_code.getKey() + phone;
        String code = redisTemplate.opsForValue().get(key);
        return StrUtil.isBlank(code) ?  true : false;
    }

    /**
     * 根據手機號取驗證碼
     * @param phone
     * @return
     */
    public String getCodeByPhone(String phone) {
        String key = RedisKeyConstant.verify_code.getKey() + phone;
        String code = redisTemplate.opsForValue().get(key);
        return code;
    }
}
