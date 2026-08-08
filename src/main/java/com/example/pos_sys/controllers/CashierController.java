package com.example.pos_sys.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cashier") // Use v1 v2 maybe if have versions /api/v1/cashier

@Tag(name = "Cashier-API")

public class CashierController {

  // Constructor injection
  public final JdbcTemplate jdbcTemplate;

  public CashierController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping
  public Map<String, Object> getAll() {

    String sql = "SELECT * FROM tb_cashiers";

    List<Map<String, Object>> cashiers = jdbcTemplate.queryForList(sql);

    /*
     * Cashier = List + Map
     * [
     * {id: 1, name: john ...}
     * {id: 2, name: jack ...}
     * ]
     */

    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("data", cashiers);

    return response;
  }

  @PostMapping
  public Map<String, Object> create(@RequestBody Map<String, Object> body) {
    // get data from frontend
    Object f = body.get("fullname");
    Object p = body.get("phone");
    Object u = body.get("username");

    // to insert to sql
    String sql = "INSERT INTO tb_cashiers (fullname, phone, username) VALUES (?,?,?)";
    jdbcTemplate.update(sql, f, p, u);

    return Map.of("message", "Create success");
  }

  @PutMapping("{id}")
  public Map<String, Object> update(@RequestBody Map<String, Object> body, @PathVariable long id) {
    // get data from frontend
    Object f = (String) body.get("fullname");
    Object p = (String) body.get("phone");
    Object u = (String) body.get("username");

    // update db
    String sql = "UPDATE tb_cashiers SET fullname = ?, phone = ?, username = ? WHERE id = ?";
    jdbcTemplate.update(sql, f, p, u, id);

    return Map.of("message", "Update success");
  }

  @DeleteMapping("{id}")
  public Map<String, Object> delete(@PathVariable long id) {

    String sql = "DELETE FROM tb_cashiers WHERE id = ?";

    // To delete from sql
    jdbcTemplate.update(sql, id);

    return Map.of("message", "Delete success");
  }
}
