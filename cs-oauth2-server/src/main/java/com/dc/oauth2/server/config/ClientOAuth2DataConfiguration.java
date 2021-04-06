package com.dc.oauth2.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客戶端配置類
 */
@Component
@ConfigurationProperties(prefix = "client.oauth2")
@Data
public class ClientOAuth2DataConfiguration {

    // 客戶端標識 ID
    private String clientId;

    // 客戶端安全碼
    private String secret;

    // 授權類型
    private String[] grantTypes;

    // token有效期
    private int tokenValidityTime;

    // refresh-token有效期
    private int refreshTokenValidityTime;

    // 客戶端訪問範圍
    private String[] scopes;

}
