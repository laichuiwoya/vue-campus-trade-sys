package com.campus.trade.interceptor;

import com.campus.trade.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register") || path.equals("/api/ping") ||
                (path.startsWith("/api/goods") && "GET".equalsIgnoreCase(request.getMethod())) ||
                (path.startsWith("/api/categories") && "GET".equalsIgnoreCase(request.getMethod())) ||
                (path.startsWith("/api/comments") && "GET".equalsIgnoreCase(request.getMethod()))) {
            return true;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Unauthorized\"}");
            return false;
        }

        try {
            String token = auth.substring(7);
            Claims claims = JwtUtil.parseToken(token);
            String role = claims.get("role", String.class);
            if (path.startsWith("/api/admin") && !"ADMIN".equals(role)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"Admin permission required\"}");
                return false;
            }
            request.setAttribute("userId", Long.valueOf(claims.getSubject()));
            request.setAttribute("username", claims.get("username", String.class));
            request.setAttribute("role", role);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token invalid\"}");
            return false;
        }
    }
}
