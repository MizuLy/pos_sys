package com.example.pos_sys.dtos.products;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequestDTO {

  @NotBlank(message = "Product name is required")
  @Size(max = 100, message = "Product name must be under 100 characters")
  private String product_name;

  @NotNull(message = "Category ID is required")
  private Long category_id;

  @NotNull(message = "Category ID is required")
  @Digits(integer = 10, fraction = 2)
  @DecimalMin(value = "0.0", inclusive = false)
  // inclusive = false price must be > 0.0
  // inclusive = true price must be >= 0.0
  private BigDecimal price;
}
