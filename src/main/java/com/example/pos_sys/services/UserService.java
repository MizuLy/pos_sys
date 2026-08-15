package com.example.pos_sys.services;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pos_sys.dtos.users.LoginRequestDTO;
import com.example.pos_sys.dtos.users.RegisterRequestDTO;
import com.example.pos_sys.dtos.users.RegisterResponseDTO;
import com.example.pos_sys.mappers.UserMapper;
import com.example.pos_sys.models.User;
import com.example.pos_sys.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public UserService(UserRepository userRepository, UserMapper userMapper, JwtService jwtService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.jwtService = jwtService;
  }

  public RegisterResponseDTO register(RegisterRequestDTO dto) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("Email already registered");
    }

    User user = userMapper.toEntity(dto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

    String token = jwtService.generateToken(user.getEmail(), user.getRole());

    return new RegisterResponseDTO(user.getId(), token);
  }

  public RegisterResponseDTO login(LoginRequestDTO dto) {
    User user = userRepository.findByEmail(dto.getEmail());
    if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }

    String token = jwtService.generateToken(user.getEmail(), user.getRole());

    return new RegisterResponseDTO(user.getId(), token);
  }

  public Map<String, Object> me(String token) {
    String email = jwtService.extractEmail(token);

    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new EntityNotFoundException("User not found");
    }

    return Map.of("id", user.getId(),
        "email", user.getEmail(),
        "role", user.getRole());
  }
}
