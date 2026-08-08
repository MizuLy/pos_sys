package com.example.pos_sys.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos_sys.models.Category;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
@Tag(name = "CRUD API Controller + Model")
public class CategoryController {
  private final JdbcTemplate jdbcTemplate;

  public CategoryController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping
  public Map<String, Object> getAll() {
    String sql = "SELECT * FROM tb_categories";

    // return as array object
    List<Category> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));

    Map<String, Object> res = new HashMap<>();
    res.put("status", 200);
    res.put("data", categories);

    return res;
  }

  @PostMapping
  public Map<String, Object> create(@Valid @RequestBody Category category) {
    String sql = "INSERT INTO tb_categories (category_name) VALUES (?)";

    jdbcTemplate.update(sql, category.getCategory_name());

    return Map.of("message", "Create success");
  }

  @PutMapping("{id}")
  public Map<String, Object> update(@PathVariable Long id, @Valid @RequestBody Category category) {
    String sql = "UPDATE tb_categories SET category_name = ? WHERE id = ?";

    jdbcTemplate.update(sql, category.getCategory_name(), id);

    return Map.of("message", "Update success");
  }

  @DeleteMapping("{id}")
  public Map<String, Object> delete(@PathVariable Long id) {
    String sql = "DELETE FROM tb_categories WHERE id = ?";

    jdbcTemplate.update(sql, id);

    return Map.of("message", "Delete success");
  }
}
