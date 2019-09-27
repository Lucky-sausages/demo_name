package com.example.demoname.interceptor;

import com.example.demoname.controller.ApiController;
import com.example.demoname.service.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public AuthorizationInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (ApiController.class.isAssignableFrom(method.getDeclaringClass())) {
                String authorization = request.getHeader("Authorization");
                if (authorization != null && authorization.startsWith(AUTHORIZATION_PREFIX)) {
                    String token = authorization.substring(AUTHORIZATION_PREFIX.length()).trim();
                    jwtService.find(token).ifPresent(user -> request.setAttribute("user", user));
                }
            }
        }

        return true;
    }
}
