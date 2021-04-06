package com.dc.diners.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客戶端配置類
 */
@Component
@ConfigurationProperties(prefix = "oauth2.client")
@Getter
@Setter
public class OAuth2ClientConfiguration {

    private String clientId;
    private String secret;
    private String grant_type;
    private String scope;

}
