package com.example.pos_sys.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@jakarta.persistence.Table(name = "tb_products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_name", nullable = false, length = 100)
  private String product_name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Category category;

  @Column(nullable = false, length = 12)
  private BigDecimal price;
}

/*
 * 
 * tb_products
 * 
 * int id autoincrement
 * varchar product_name (100,not null)
 * int category_id (fk join table Category)
 * decimal price (12, not null)
 * 
 * 
 */