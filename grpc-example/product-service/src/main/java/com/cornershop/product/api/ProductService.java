package com.cornershop.product.api;

import com.cornershop.product.appliction.ProductInfo;
import com.cornershop.product.appliction.repository.ProductRepository;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {

  private final ProductRepository productRepository;

  public ProductService() {
    this.productRepository = new ProductRepository();
  }

  @Override
  public void getProduct(
          Resources.GetProductRequest request, StreamObserver<Resources.GetProductResponse> responseObserver) {

    log.info("Calling product repository..");

    var productId = request.getProductId();
    Optional<ProductInfo> productInfo = productRepository.get(productId);

    if (productInfo.isPresent()) {
      var product = productInfo.get();

      var getProductResponse =
          Resources.GetProductResponse.newBuilder()
              .setName(product.getName())
              .setDescription(product.getDescription())
              .setPrice(product.getPrice())
              .build();
      responseObserver.onNext(getProductResponse);
      responseObserver.onCompleted();
    } else {
      responseObserver.onError(new StatusException(Status.NOT_FOUND));
    }

    log.info("Finished calling Product API service..");
  }
}
