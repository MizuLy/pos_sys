package com.example.pos_sys.mappers;

import org.springframework.stereotype.Component;

import com.example.pos_sys.dtos.products.ProductRequestDTO;
import com.example.pos_sys.dtos.products.ProductResponseDTO;
import com.example.pos_sys.models.Category;
import com.example.pos_sys.models.Product;
import com.example.pos_sys.repositories.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class ProductMapper {
  private final CategoryRepository categoryRepository;

  private ProductMapper(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Product toEntity(ProductRequestDTO dto) {
    Product product = new Product();

    product.setProduct_name(dto.getProduct_name());
    product.setPrice(dto.getPrice());
    product.setCategory(resolveCategory(dto.getCategory_id()));

    // produt: {product_name, price, category_id}
    return product;
  }

  // ====================== old data ====== new data
  public void updateEntity(Product product, ProductRequestDTO dto) {
    product.setProduct_name(dto.getProduct_name());
    product.setPrice(dto.getPrice());
    product.setCategory(resolveCategory(dto.getCategory_id()));
  }

  public ProductResponseDTO toResponse(Product product) {

    ProductResponseDTO dto = new ProductResponseDTO();

    dto.setId(product.getId());
    dto.setProduct_name(product.getProduct_name());
    dto.setPrice(product.getPrice());

    Category category = product.getCategory(); // {id,categoryname}''

    // 1 != null => 1
    if (category.getId() != null) {
      dto.setCategory_id(category.getId());
      dto.setCategory_name(category.getCategory_name());
    }

    return dto; // {id, product_name, price,categoyr_id, category_name}
  }

  private Category resolveCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("Category id not found: " + categoryId));
  }
}
