package com.example.pos_sys.config;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.pos_sys.services.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

  private final JwtService jwtService;

  public AuthInterceptor(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    String authHeader = request.getHeader("Authorization");
    String token = (authHeader != null && authHeader.startsWith("Bearer "))
        ? authHeader.substring(7)
        : null;

    if (token == null || !jwtService.isValid(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.setCharacterEncoding(StandardCharsets.UTF_8.name());
      response.getWriter().write("{\"status\":\"Error\",\"message\":\"Unauthorized\"}");
      return false;
    }

    return true;
  }
}
