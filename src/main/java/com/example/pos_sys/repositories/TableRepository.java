package com.example.pos_sys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos_sys.models.Table;

public interface TableRepository extends JpaRepository<Table, Long> {

}
