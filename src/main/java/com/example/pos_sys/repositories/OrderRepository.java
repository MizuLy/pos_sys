package com.example.pos_sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos_sys.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
