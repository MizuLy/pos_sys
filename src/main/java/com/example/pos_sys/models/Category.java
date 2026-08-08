package com.example.pos_sys.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@jakarta.persistence.Table(name = "tb_categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Validation from library (Validation)
  @NotBlank(message = "Field must not be empty")
  @Size(max = 50, message = "Category name must be under 50 characters")
  private String category_name;

  // If no lombok then use (get, set)

  // public void setCategory(String category) {
  // this.category_name = category;
  // }

  // public String getCategory_name() {
  // return category_name;
  // }
}
