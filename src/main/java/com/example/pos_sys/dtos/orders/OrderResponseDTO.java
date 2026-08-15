package com.example.pos_sys.dtos.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponseDTO {
  private Long id;

  private Long table_id;

  private String table_name;

  private Long cashier_id;

  private Integer queue_no;

  private LocalDateTime time_in;

  private LocalDateTime time_out;

  private String payment_method;

  private BigDecimal subtotal;

  private List<OrderItemResponseDTO> items;
}
