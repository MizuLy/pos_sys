package com.example.pos_sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos_sys.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}