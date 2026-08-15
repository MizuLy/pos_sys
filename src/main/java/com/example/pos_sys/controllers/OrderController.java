package com.example.pos_sys.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos_sys.dtos.orders.OrderRequestDTO;
import com.example.pos_sys.services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public Map<String, Object> getAll() {
    Map<String, Object> res = new HashMap<>();
    res.put("status", "success");
    res.put("data", orderService.getAll());
    return res;
  }

  @GetMapping("{id}")
  public Map<String, Object> getOne(@PathVariable Long id) {
    try {
      Map<String, Object> res = new HashMap<>();
      res.put("status", "success");
      res.put("data", orderService.getById(id));
      return res;
    } catch (EntityNotFoundException e) {
      return Map.of("status", 404, "message", e.getMessage());
    }
  }

  @PostMapping
  public Map<String, Object> create(@Valid @RequestBody OrderRequestDTO dto) {
    try {
      return Map.of("message", "Order created successfully", "data", orderService.create(dto));
    } catch (EntityNotFoundException e) {
      return Map.of("status", "Error", "message", e.getMessage());
    }
  }

  @PutMapping("{id}")
  public Map<String, Object> update(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO dto) {
    try {
      return Map.of("message", "Order updated successfully", "data", orderService.update(id, dto));
    } catch (EntityNotFoundException e) {
      return Map.of("status", "Error", "message", e.getMessage());
    }
  }

  @DeleteMapping("{id}")
  public Map<String, Object> delete(@PathVariable Long id) {
    try {
      orderService.delete(id);
      return Map.of("message", "Order deleted successfully");
    } catch (EntityNotFoundException e) {
      return Map.of("status", 404, "message", e.getMessage());
    }
  }
}
