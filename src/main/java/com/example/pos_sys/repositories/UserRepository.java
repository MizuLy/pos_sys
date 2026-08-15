package com.example.pos_sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos_sys.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  User findByEmail(String email);
}
