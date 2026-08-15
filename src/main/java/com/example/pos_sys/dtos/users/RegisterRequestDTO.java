package com.example.pos_sys.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  @Size(max = 50, message = "Email must be under 50 characters")
  private String email; // front need to use this key

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
  private String password;
}
