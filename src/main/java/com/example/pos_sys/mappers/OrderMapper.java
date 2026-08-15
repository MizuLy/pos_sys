package com.example.pos_sys.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.pos_sys.dtos.orders.OrderItemRequestDTO;
import com.example.pos_sys.dtos.orders.OrderItemResponseDTO;
import com.example.pos_sys.dtos.orders.OrderRequestDTO;
import com.example.pos_sys.dtos.orders.OrderResponseDTO;
import com.example.pos_sys.models.Order;
import com.example.pos_sys.models.OrderDetail;
import com.example.pos_sys.models.Product;
import com.example.pos_sys.models.Table;
import com.example.pos_sys.repositories.ProductRepository;
import com.example.pos_sys.repositories.TableRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class OrderMapper {
  private final TableRepository tableRepository;
  private final ProductRepository productRepository;

  private OrderMapper(TableRepository tableRepository, ProductRepository productRepository) {
    this.tableRepository = tableRepository;
    this.productRepository = productRepository;
  }

  public Order toEntity(OrderRequestDTO dto) {
    Order order = new Order();
    applyCommon(order, dto);
    order.setTime_in(dto.getTime_in() != null ? dto.getTime_in() : LocalDateTime.now());
    order.setTime_out(dto.getTime_out());
    order.setOrder_details(buildDetails(order, dto.getItems()));
    order.setSubtotal(computeSubtotal(order.getOrder_details()));
    return order;
  }

  // ====================== old data ====== new data
  public void updateEntity(Order order, OrderRequestDTO dto) {
    applyCommon(order, dto);
    order.setTime_in(dto.getTime_in());
    order.setTime_out(dto.getTime_out());
    order.getOrder_details().clear();
    order.getOrder_details().addAll(buildDetails(order, dto.getItems()));
    order.setSubtotal(computeSubtotal(order.getOrder_details()));
  }

  public OrderResponseDTO toResponse(Order order) {
    OrderResponseDTO dto = new OrderResponseDTO();

    dto.setId(order.getId());

    Table table = order.getTable();
    if (table != null) {
      dto.setTable_id(table.getId());
      dto.setTable_name(table.getTable_name());
    }

    dto.setCashier_id(order.getCashier_id());
    dto.setQueue_no(order.getQueue_no());
    dto.setTime_in(order.getTime_in());
    dto.setTime_out(order.getTime_out());
    dto.setPayment_method(order.getPayment_method());
    dto.setSubtotal(order.getSubtotal());
    dto.setItems(order.getOrder_details().stream().map(this::toItemResponse).collect(Collectors.toList()));

    return dto;
  }

  private void applyCommon(Order order, OrderRequestDTO dto) {
    order.setTable(resolveTable(dto.getTable_id()));
    order.setCashier_id(dto.getCashier_id());
    order.setQueue_no(dto.getQueue_no());
    order.setPayment_method(dto.getPayment_method());
  }

  private List<OrderDetail> buildDetails(Order order, List<OrderItemRequestDTO> items) {
    return items.stream().map(item -> toDetailEntity(order, item)).collect(Collectors.toList());
  }

  private OrderDetail toDetailEntity(Order order, OrderItemRequestDTO dto) {
    OrderDetail detail = new OrderDetail();

    Product product = resolveProduct(dto.getProduct_id());
    BigDecimal unitPrice = dto.getUnit_price() != null ? dto.getUnit_price() : product.getPrice();
    BigDecimal discount = dto.getDiscount_percent() != null ? dto.getDiscount_percent() : BigDecimal.ZERO;

    detail.setOrder(order);
    detail.setProduct(product);
    detail.setQty(dto.getQty());
    detail.setUnit_price(unitPrice);
    detail.setDiscount_percent(discount);
    detail.setTotal(computeLineTotal(unitPrice, dto.getQty(), discount));

    return detail;
  }

  private OrderItemResponseDTO toItemResponse(OrderDetail detail) {
    OrderItemResponseDTO dto = new OrderItemResponseDTO();

    dto.setId(detail.getId());

    Product product = detail.getProduct();
    if (product != null) {
      dto.setProduct_id(product.getId());
      dto.setProduct_name(product.getProduct_name());
    }

    dto.setQty(detail.getQty());
    dto.setUnit_price(detail.getUnit_price());
    dto.setDiscount_percent(detail.getDiscount_percent());
    dto.setTotal(detail.getTotal());

    return dto;
  }

  private Table resolveTable(Long tableId) {
    return tableRepository.findById(tableId)
        .orElseThrow(() -> new EntityNotFoundException("Table id not found: " + tableId));
  }

  private Product resolveProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("Product id not found: " + productId));
  }

  // total = unit_price * qty * (1 - discount_percent/100)
  private BigDecimal computeLineTotal(BigDecimal unitPrice, Integer qty, BigDecimal discountPercent) {
    BigDecimal factor = BigDecimal.ONE
        .subtract(discountPercent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
    return unitPrice.multiply(BigDecimal.valueOf(qty)).multiply(factor).setScale(2, RoundingMode.HALF_UP);
  }

  private BigDecimal computeSubtotal(List<OrderDetail> details) {
    return details.stream().map(OrderDetail::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
