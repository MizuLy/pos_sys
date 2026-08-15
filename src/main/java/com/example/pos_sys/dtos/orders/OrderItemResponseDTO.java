package com.example.pos_sys.dtos.orders;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponseDTO {
  private Long id;

  private Long product_id;

  private String product_name;

  private Integer qty;

  private BigDecimal unit_price;

  private BigDecimal discount_percent;

  private BigDecimal total;
}
