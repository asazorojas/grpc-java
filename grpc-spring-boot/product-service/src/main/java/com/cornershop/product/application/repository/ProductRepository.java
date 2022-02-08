package com.cornershop.product.application.repository;

import com.cornershop.product.application.Product;

public interface ProductRepository {

  /**
   * Get product based on the productId. Throws ResourceNotFoundException if product id is not found.
   *
   * @param productId id of the product.
   * @return Product
   */
  Product get(String productId) ;

  String save(Product product);
}
