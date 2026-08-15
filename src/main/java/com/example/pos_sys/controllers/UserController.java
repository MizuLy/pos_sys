package com.example.pos_sys.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos_sys.dtos.users.LoginRequestDTO;
import com.example.pos_sys.dtos.users.RegisterRequestDTO;
import com.example.pos_sys.dtos.users.RegisterResponseDTO;
import com.example.pos_sys.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Register users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public Map<String, Object> register(@Valid @RequestBody RegisterRequestDTO dto) {
    try {
      RegisterResponseDTO data = userService.register(dto);
      return Map.of("status", "success",
          "message", "User registered",
          "data", data);
    } catch (IllegalArgumentException e) {
      return Map.of("status", "error", "message", e.getMessage());
    }
  }

  @PostMapping("/login")
  public Map<String, Object> login(@Valid @RequestBody LoginRequestDTO dto) {
    try {
      RegisterResponseDTO data = userService.login(dto);
      return Map.of("status", "success",
          "message", "User logged in",
          "data", data);
    } catch (IllegalArgumentException e) {
      return Map.of("status", "error", "message", e.getMessage());
    }
  }

  @GetMapping("/me")
  @SecurityRequirement(name = "bearerAuth")
  public Map<String, Object> me(@RequestHeader("Authorization") String authHeader) {
    try {
      String token = authHeader.replace("Bearer ", "");
      Map<String, Object> data = userService.me(token);
      return Map.of("status", "Success", "data", data);
    } catch (Exception e) {
      return Map.of("status", "error", "message", e.getMessage());
    }
  }
}
