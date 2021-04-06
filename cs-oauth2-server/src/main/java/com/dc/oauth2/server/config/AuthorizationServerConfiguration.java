package com.dc.oauth2.server.config;

import com.dc.commons.model.domain.SignInIdentity;
import com.dc.oauth2.server.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;

/**
 * 授權服務
 * */
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * 授權服務
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // 密碼編碼器
    @Resource
    private PasswordEncoder passwordEncoder;
    // 客戶端配置類
    @Resource
    private ClientOAuth2DataConfiguration clientOAuth2DataConfiguration;
    // 認證管理
    @Resource
    private AuthenticationManager authenticationManager;
    // 將 Token 存儲至 Redis
    @Resource
    private RedisTokenStore redisTokenStore;
    // 登錄校驗
    @Resource
    private UserService userService;

    /**
     * 配置令牌端點(Token Endpoint)的安全約束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允許訪問 Token 的公鑰，默認 /oauth/token_key 是受保護的
        security.tokenKeyAccess("permitAll()")
                // 允許檢查 Token 狀態，默認 /oauth/check_token 是受保護的
                .checkTokenAccess("permitAll()");
    }

    /**
     * 客戶端配置 - 授權模型
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 客戶端標識 id
        clients.inMemory().withClient(clientOAuth2DataConfiguration.getClientId())
                // 客戶端安全碼
                .secret(passwordEncoder.encode(clientOAuth2DataConfiguration.getSecret()))
                // 授權類型
                .authorizedGrantTypes(clientOAuth2DataConfiguration.getGrantTypes())
                // Token 有效時間
                .accessTokenValiditySeconds(clientOAuth2DataConfiguration.getTokenValidityTime())
                // 刷新 Token 的有效時間
                .refreshTokenValiditySeconds(clientOAuth2DataConfiguration.getRefreshTokenValidityTime())
                // 客戶端訪問範圍
                .scopes(clientOAuth2DataConfiguration.getScopes());
    }

    /**
     * 配置授權（authorization）以及令牌（token）的訪問端點和令牌服務(token services)
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 認證器
        endpoints.authenticationManager(authenticationManager)
                // 具體登錄的方法
                .userDetailsService(userService)
                // token 存儲的方式：內存、redis、數據庫、jwt 等
                .tokenStore(redisTokenStore)
                // Token增強對象，增強返回的結果
                .tokenEnhancer((accessToken, authentication) -> {
                    //取得登入後，使用者的相關資訊，然後設置
                    SignInIdentity signInIdentity = (SignInIdentity) authentication.getPrincipal();
                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                    map.put("nickname", signInIdentity.getNickname());
                    map.put("avatarUrl", signInIdentity.getAvatarUrl());
                    DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
                    token.setAdditionalInformation(map);
                    return token;
                });
    }

}
