package com.example.pos_sys.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos_sys.models.Table;
import com.example.pos_sys.repositories.TableRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tables")
@Tag(name = "Table API")
public class TableController {

  private final TableRepository tableRepository;

  public TableController(TableRepository tableRepository) {
    this.tableRepository = tableRepository;
  }

  @GetMapping
  public Map<String, Object> getAll() {
    Map<String, Object> res = new HashMap<>();
    res.put("status", "success");
    res.put("data", tableRepository.findAll());
    return res;
  }

  @GetMapping("{id}")
  public Map<String, Object> getOne(@PathVariable Long id) {
    Optional<Table> found = tableRepository.findById(id);
    if (found.isEmpty()) {
      return Map.of("status", 404, "message", "Table not found");
    }
    Map<String, Object> res = new HashMap<>();
    res.put("status", 200);
    res.put("data", found.get());
    return res;
  }

  @PostMapping
  public Map<String, Object> create(@Valid @RequestBody Table table) {
    table.setId(null);
    return Map.of("message", "Table inserted successfully", "data", tableRepository.save(table));
  }

  @PutMapping("{id}")
  public Map<String, Object> update(@PathVariable Long id, @Valid @RequestBody Table table) {
    if (!tableRepository.existsById(id)) {
      return Map.of("message", "Table not found");
    }

    table.setId(id);
    return Map.of("message", "Table updated successfully", "data", tableRepository.save(table));
  }

  @DeleteMapping("{id}")
  public Map<String, Object> delete(@PathVariable long id) {
    if (!tableRepository.existsById(id)) {
      return Map.of("message", "Table not found");
    }
    tableRepository.deleteById(id);
    return Map.of("message", "Table deleted successfully");
  }
}
