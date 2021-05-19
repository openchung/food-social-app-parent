package com.dc.seckill.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
public class RedisLock {

    private RedisTemplate redisTemplate;
    private DefaultRedisScript<Long> lockScript;
    private DefaultRedisScript<Object> unlockScript;

    public RedisLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        // 加載釋放鎖的腳本
        this.lockScript = new DefaultRedisScript<>();
        this.lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock.lua")));
        this.lockScript.setResultType(Long.class);
        // 加載釋放鎖的腳本
        this.unlockScript = new DefaultRedisScript<>();
        this.unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("unlock.lua")));
    }

    /**
     * 獲取鎖
     * @param lockName 鎖名稱
     * @param releaseTime 超時時間(單位:秒)
     * @return key 解鎖標識
     */
    public String tryLock(String lockName, long releaseTime) {
        // 存入的線程信息的前綴，防止與其它JVM中線程信息沖突
        String key = UUID.randomUUID().toString();

        // 執行腳本
        Long result = (Long)redisTemplate.execute(
                lockScript,
                Collections.singletonList(lockName),
                key + Thread.currentThread().getId(), releaseTime);

        // 判斷結果
        if(result != null && result.intValue() == 1) {
            return key;
        }else {
            return null;
        }
    }
    /**
     * 釋放鎖
     * @param lockName 鎖名稱
     * @param key 解鎖標識
     */
    public void unlock(String lockName, String key) {
        // 執行腳本
        redisTemplate.execute(
                unlockScript,
                Collections.singletonList(lockName),
                key + Thread.currentThread().getId(), null);
    }
}