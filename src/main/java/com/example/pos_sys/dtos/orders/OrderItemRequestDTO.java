package com.example.pos_sys.dtos.orders;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {

  @NotNull(message = "Product ID is required")
  private Long product_id;

  @NotNull(message = "Quantity is required")
  @Min(value = 1, message = "Quantity must be at least 1")
  private Integer qty;

  // optional -> falls back to the product's price
  @Digits(integer = 10, fraction = 2)
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal unit_price;

  // optional -> defaults to 0
  @DecimalMin(value = "0.0")
  @DecimalMax(value = "100.0")
  @Digits(integer = 3, fraction = 2)
  private BigDecimal discount_percent;
}
