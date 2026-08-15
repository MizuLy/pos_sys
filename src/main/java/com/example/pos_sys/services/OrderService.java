package com.example.pos_sys.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pos_sys.dtos.orders.OrderRequestDTO;
import com.example.pos_sys.dtos.orders.OrderResponseDTO;
import com.example.pos_sys.mappers.OrderMapper;
import com.example.pos_sys.models.Order;
import com.example.pos_sys.repositories.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  public List<OrderResponseDTO> getAll() {
    return orderRepository.findAll().stream().map(orderMapper::toResponse).collect(Collectors.toList());
  }

  public OrderResponseDTO getById(Long id) {
    return orderMapper.toResponse(findEntity(id));
  }

  @Transactional
  public OrderResponseDTO create(OrderRequestDTO dto) {
    Order saved = orderRepository.save(orderMapper.toEntity(dto));
    return orderMapper.toResponse(saved);
  }

  @Transactional
  public OrderResponseDTO update(Long id, OrderRequestDTO dto) {
    Order order = findEntity(id);
    orderMapper.updateEntity(order, dto);
    return orderMapper.toResponse(orderRepository.save(order));
  }

  @Transactional
  public void delete(Long id) {
    if (!orderRepository.existsById(id)) {
      throw new EntityNotFoundException("Order not found");
    }
    orderRepository.deleteById(id);
  }

  private Order findEntity(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
  }
}
