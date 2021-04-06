package com.dc.gateway.filter;

import com.dc.commons.utils.ResultInfoUtil;
import com.dc.gateway.component.HandleException;
import com.dc.gateway.config.IgnoreUrlsConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 網關全局過濾器
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private HandleException handleException;

    /**
     * 身份校驗處理
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判斷當前的請求是否在白名單中
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean flag = false;
        String path = exchange.getRequest().getURI().getPath();
        for (String url : ignoreUrlsConfig.getUrls()) {
            if (pathMatcher.match(url, path)) {
                flag = true;
                break;
            }
        }
        // 白名單放行
        if (flag) {
            return chain.filter(exchange);
        }
        // 獲取 access_token
        String access_token = exchange.getRequest().getQueryParams().getFirst("access_token");
        // 判斷 access_token 是否為空
        System.out.println();
        if (StringUtils.isBlank(access_token) && !exchange.getRequest().getHeaders().containsKey("Authorization")) {
            return handleException.writeError(exchange, "請登入");
        }
        if(StringUtils.isBlank(access_token)) {
            access_token = exchange.getRequest().getHeaders().get("Authorization").get(0).split(" ")[1];
        }
        // 校驗 token 是否有效
        String checkTokenUrl = "http://cs-oauth2-server/oauth/check_token?token=".concat(access_token);
        try {
            // 發送遠程請求，驗證 token
            ResponseEntity<String> entity = restTemplate.getForEntity(checkTokenUrl, String.class);
            // token 無效的業務邏輯處理
            if (entity.getStatusCode() != HttpStatus.OK) {
                return handleException.writeError(exchange,
                        "Token was not recognised, token: ".concat(access_token));
            }
            if (StringUtils.isBlank(entity.getBody())) {
                return handleException.writeError(exchange,
                        "This token is invalid: ".concat(access_token));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return handleException.writeError(exchange,
                    "Token was not recognised, token: ".concat(access_token));
        }
        // 放行
        return chain.filter(exchange);
    }

    /**
     * 網關過濾器的排序，數字越小優先級越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

}
