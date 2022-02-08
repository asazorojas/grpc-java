package com.cornershop.api.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cornershop.api.product", "com.cornershop.commons"})
public class ProductGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductGatewayApplication.class, args);
  }
}
