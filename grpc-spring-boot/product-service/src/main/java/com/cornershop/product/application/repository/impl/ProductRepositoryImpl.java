package com.cornershop.product.application.repository.impl;

import com.cornershop.commons.error.ResourceNotFoundException;
import com.cornershop.product.application.Product;
import com.cornershop.product.application.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final Map<String, Product> productStorage;

  public ProductRepositoryImpl() {
    productStorage = new HashMap<>();
  }

  public Product get(String productId) {
    var product = Optional.ofNullable(productStorage.get(productId));

    return product.orElseThrow(
        () ->
            new ResourceNotFoundException(
                "Product ID not found",
                Map.of("resource_id", productId, "message", "Product ID not found")));
  }

  @Override
  public String save(Product product) {
    var uuid = UUID.randomUUID().toString();
    productStorage.put(uuid, product);
    return uuid;
  }
}
