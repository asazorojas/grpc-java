package com.cornershop.product.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {
    private String productId;
    private String name;
    private String description;
    private double price;
}
