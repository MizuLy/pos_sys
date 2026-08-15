package com.example.pos_sys.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponseDTO {
  private Long id;
  // private String email;
  // private String role;
  private String token;
}
