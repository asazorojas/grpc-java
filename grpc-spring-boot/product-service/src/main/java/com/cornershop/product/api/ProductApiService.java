package com.cornershop.product.api;

import com.cornershop.product.Resources;
import com.cornershop.product.api.mapper.ProductMapper;
import com.cornershop.product.application.repository.ProductRepository;
import com.cornershop.product.resource.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
public class ProductApiService extends ProductServiceGrpc.ProductServiceImplBase {

  @Autowired private ProductRepository productRepository;

  @Override
  public void createProduct(
          Resources.CreateProductRequest request, StreamObserver<Resources.CreateProductResponse> responseObserver) {

    log.info("Calling Product Repository..");

    var product = ProductMapper.MAPPER.map(request);
    var productId = productRepository.save(product);

    var createProductResponse = Resources.CreateProductResponse.newBuilder().setProductId(productId).build();

    responseObserver.onNext(createProductResponse);
    responseObserver.onCompleted();

    log.info("Saved Product, Id {} ..", productId);
  }

  @Override
  public void getProduct(
          Resources.GetProductRequest request, StreamObserver<Resources.GetProductResponse> responseObserver) {

    log.info("Calling Product Repository..");

    String productId = request.getProductId();

    var product = productRepository.get(productId);

    var response =
        Resources.GetProductResponse.newBuilder()
            .setName(product.getName())
            .setDescription(product.getDescription())
            .setPrice(product.getPrice())
            .setUserId(product.getUserId())
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();

    log.info("Finished calling Product API service..");
  }
}
