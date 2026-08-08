package com.example.pos_sys.models;

import com.example.pos_sys.enums.TableEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@jakarta.persistence.Table(name = "tb_table")
public class Table {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Size(max = 50, message = "Table name must be under 50 characters") // use with frontend
  @Column(name = "table_name", length = 50) // use with JPA
  private String table_name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TableEnum status = TableEnum.AVAILABLE;
}
