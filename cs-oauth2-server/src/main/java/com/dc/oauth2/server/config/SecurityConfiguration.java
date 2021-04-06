package com.dc.oauth2.server.config;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/*
* Security配置類
* */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 注入 Redis 連接工廠
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    // 初始化 RedisTokenStore 用於將 token 存儲至 Redis
    @Bean
    public RedisTokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("TOKEN:"); // 設置key的層級前綴，方便查詢
        return redisTokenStore;
    }

    // 初始化密碼編碼氣，用 MD5 加密密碼
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            /*
            * 加密
            * @Param rawPassword        原始密碼
            * @Return
            * */
            @Override
            public String encode(CharSequence rawPassword) {
                return DigestUtil.md5Hex(rawPassword.toString());
            }

            /*
             * 校驗密碼
             * @param rawPassword       原始密碼
             * @param encodedPassword   加密密碼
             * @Return
             * */
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return DigestUtil.md5Hex(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }

    // 初始化認證管理對象
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    // 放行和認證規則
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // 放行請求
                .antMatchers("/oauth/**", "/actuator/**").permitAll()
                .and()
                .authorizeRequests()
                // 其他請求必須認證才能訪問
                .anyRequest().authenticated();
    }
}
