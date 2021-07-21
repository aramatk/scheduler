package com.scheduler.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Value(value = "${app.http-requests.key}")
    private String KEY;
    private static final String[] WHITELIST_EQUALS = {
            "/",
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
    };
    private static final String[] WHITELIST_CONTAINS = {
            "/swagger-resources",
            "/webjars",
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (Arrays.asList(WHITELIST_EQUALS).stream().anyMatch(path::equals)
                || Arrays.asList(WHITELIST_CONTAINS).stream().anyMatch(path::contains)) {
            return true;
        }
        var key = request.getHeader("key");
        if (key == null || key.isBlank() || !key.equals(KEY)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
