package com.example.pos_sys.mappers;

import org.springframework.stereotype.Component;

import com.example.pos_sys.dtos.users.RegisterRequestDTO;
import com.example.pos_sys.models.User;

@Component
public class UserMapper {
  public User toEntity(RegisterRequestDTO dto) {
    User user = new User();

    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    user.setRole("cashier");

    return user;
  }
}
