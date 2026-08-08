package com.example.pos_sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos_sys.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}