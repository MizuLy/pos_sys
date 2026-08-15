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
@jakarta.persistence.Table(name = "tb_order_details")
public class OrderDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Product product;

  @Column(name = "qty")
  private Integer qty;

  @Column(name = "unit_price", precision = 12, scale = 2)
  private BigDecimal unit_price;

  @Column(name = "discount_percent", precision = 12, scale = 2)
  private BigDecimal discount_percent;

  @Column(name = "total", precision = 12, scale = 2)
  private BigDecimal total;
}

/*
 * 
 * tb_order_details
 * 
 * bigint id autoincrement
 * bigint order_id (fk join table tb_orders)
 * bigint product_id (fk join table tb_products)
 * int qty
 * decimal unit_price
 * decimal discount_percent
 * decimal total
 * 
 * 
 */
