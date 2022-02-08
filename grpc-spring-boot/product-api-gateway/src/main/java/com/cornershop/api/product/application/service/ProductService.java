package com.cornershop.api.product.application.service;


import com.cornershop.api.product.application.model.Product;

public interface ProductService {
    String createNewProduct(Product product);
    Product getProduct(String productId);
}
