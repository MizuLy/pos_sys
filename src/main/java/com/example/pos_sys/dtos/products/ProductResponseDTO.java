package com.example.pos_sys.dtos.products;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductResponseDTO {
  private Long id;

  private String product_name;

  private Long category_id;

  private String category_name;

  private BigDecimal price;
}
