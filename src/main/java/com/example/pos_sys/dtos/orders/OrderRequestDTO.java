package com.example.pos_sys.dtos.orders;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequestDTO {

  @NotNull(message = "Table ID is required")
  private Long table_id;

  @NotNull(message = "Cashier ID is required")
  private Long cashier_id;

  @Min(value = 1, message = "Queue number must be at least 1")
  private Integer queue_no;

  private LocalDateTime time_in;

  private LocalDateTime time_out;

  @NotBlank(message = "Payment method is required")
  @Size(max = 20, message = "Payment method must be under 20 characters")
  private String payment_method;

  // subtotal is computed server-side from the item lines

  @Valid
  @NotNull(message = "Order must have at least one item")
  @Size(min = 1, message = "Order must have at least one item")
  private List<OrderItemRequestDTO> items;
}
