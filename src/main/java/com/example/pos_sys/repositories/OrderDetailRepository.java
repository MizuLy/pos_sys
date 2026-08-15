package com.example.pos_sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos_sys.models.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
