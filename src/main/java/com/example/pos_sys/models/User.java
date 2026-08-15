package com.example.pos_sys.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@jakarta.persistence.Table(name = "tb_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", nullable = false, unique = true, length = 50)
  @NotBlank
  @Email(message = "Email must be valid")
  @Size(max = 50, message = "Email must be under 50 characters")
  private String email;

  @Column(name = "password", nullable = false, length = 100)
  @NotBlank
  @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
  private String password;

  @Column(name = "roles", nullable = false, length = 20)
  @NotBlank
  @Size(max = 20, message = "Role must be under 20 characters")
  private String role = "cashier";

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
