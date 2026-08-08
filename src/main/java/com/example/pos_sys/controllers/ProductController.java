package com.example.pos_sys.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos_sys.dtos.products.ProductRequestDTO;
import com.example.pos_sys.dtos.products.ProductResponseDTO;
import com.example.pos_sys.mappers.ProductMapper;
import com.example.pos_sys.models.Product;
import com.example.pos_sys.repositories.ProductRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Products API controller + model + jpa + dtos + mapper", description = "Manage products")
public class ProductController {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductController(ProductRepository productRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  @GetMapping
  public Map<String, Object> getAll() {
    List<ProductResponseDTO> data = productRepository.findAll().stream().map(productMapper::toResponse)
        .collect(Collectors.toList());

    Map<String, Object> res = new HashMap<>();
    res.put("data", "success");
    res.put("data", data);
    return res;
  }

  @GetMapping("{id}")
  public Map<String, Object> getOne(@PathVariable Long id) {
    Optional<Product> found = productRepository.findById(id);
    if (found.isEmpty()) {
      return Map.of("status", "Error", "message", "Product not found");
    }

    Map<String, Object> res = new HashMap<>();
    res.put("status", "success");
    res.put("data", productMapper.toResponse(found.get()));
    return res;
  }

  @PostMapping
  public Map<String, Object> create(@Valid @RequestBody ProductRequestDTO dto) {
    try {
      Product saved = productRepository.save(productMapper.toEntity(dto));
      return Map.of("message", "Product created successfully", "data", productMapper.toResponse(saved));
    } catch (EntityNotFoundException e) {
      return Map.of("status", "Error", "message", e.getMessage());
    }
  }

  @PutMapping("{id}")
  public Map<String, Object> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
    Optional<Product> found = productRepository.findById(id);
    if (found.isEmpty()) {
      return Map.of("status", 404, "message", "Product not found");
    }

    try {
      Product product = found.get();
      productMapper.updateEntity(product, dto);
      Product saved = productRepository.save(product);
      return Map.of("message", "Product updated successfully", "data", productMapper.toResponse(saved));
    } catch (EntityNotFoundException e) {
      return Map.of("status", "Error", "message", e.getMessage());
    }
  }

  @DeleteMapping("{id}")
  public Map<String, Object> delete(@PathVariable long id) {
    if (!productRepository.existsById(id)) {
      return Map.of("status", 404, "message", "Product not found");
    }

    productRepository.deleteById(id);
    return Map.of("message", "Product deleted successfully");
  }
}
