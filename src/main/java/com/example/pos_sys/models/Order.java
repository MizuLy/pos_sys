package com.example.pos_sys.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@jakarta.persistence.Table(name = "tb_orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "table_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Table table;

  // tb_cashiers is not a JPA entity (raw JDBC only) -> plain FK column
  @Column(name = "cashier_id")
  private Long cashier_id;

  @Column(name = "queue_no")
  private Integer queue_no;

  @Column(name = "time_in")
  private LocalDateTime time_in;

  @Column(name = "time_out")
  private LocalDateTime time_out;

  @Column(name = "payment_method", length = 20)
  private String payment_method;

  @Column(name = "subtotal", precision = 12, scale = 2)
  private BigDecimal subtotal;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<OrderDetail> order_details = new ArrayList<>();
}

/*
 * 
 * tb_orders
 * 
 * bigint id autoincrement
 * bigint table_id (fk join table tb_table)
 * bigint cashier_id (fk join table tb_cashiers)
 * int queue_no
 * datetime time_in
 * datetime time_out
 * varchar payment_method
 * decimal subtotal
 * 
 * 
 */
