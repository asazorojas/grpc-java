package com.cornershop.api.product.application.service.impl;

import com.cornershop.api.product.application.error.ServiceExceptionMapper;
import com.cornershop.api.product.application.mapper.ProductMapper;
import com.cornershop.api.product.application.model.Product;
import com.cornershop.api.product.application.service.ProductService;
import com.cornershop.product.Resources;
import com.cornershop.product.resource.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ManagedChannel managedChannel;

  public ProductServiceImpl(ManagedChannel managedChannel) {
    this.managedChannel = managedChannel;
  }

  @Override
  public String createNewProduct(Product product) {
    log.info("Calling Server..");

    var createProductRequest =
        Resources.CreateProductRequest.newBuilder()
            .setPrice(product.getPrice())
            .setName(product.getName())
            .setDescription(product.getDescription())
            .setUserId(product.getUserId())
            .build();

    var productApiServiceBlockingStub = ProductServiceGrpc.newBlockingStub(managedChannel);

    var response = productApiServiceBlockingStub.createProduct(createProductRequest);

    var productId = response.getProductId();

    log.info("Received Product information from product service, info {}", product);

    return productId;
  }

  @Override
  public Product getProduct(String productId) {
    log.info("Calling Server..");
    Product product;
    try {
      var request = Resources.GetProductRequest.newBuilder().setProductId(productId).build();
      var productApiServiceBlockingStub = ProductServiceGrpc.newBlockingStub(managedChannel);
      var response = productApiServiceBlockingStub.getProduct(request);
      // Map to domain object
      product = ProductMapper.MAPPER.map(response);
      log.info("Received Product information from product service, info {}", product);
    } catch (StatusRuntimeException error) {
      log.info("Error while calling product service, reason {} ", error.getMessage());
      throw ServiceExceptionMapper.map(error);
    }
    return product;
  }
}
