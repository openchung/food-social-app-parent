package com.dc.oauth2.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dc.commons.constant.ApiConstant;
import com.dc.commons.model.domain.ResultInfo;
import com.dc.commons.utils.ResultInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 認證失敗處理
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 返回 JSON
        response.setContentType("application/json;charset=utf-8");
        // 狀態碼 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 寫出
        PrintWriter out = response.getWriter();
        String errorMessage = authException.getMessage();
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = "登錄失效!";
        }
        ResultInfo result = ResultInfoUtil.buildError(ApiConstant.ERROR_CODE,
                errorMessage, request.getRequestURI());
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
        out.close();
    }

}
