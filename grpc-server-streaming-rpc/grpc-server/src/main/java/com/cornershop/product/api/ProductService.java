package com.cornershop.product.api;

import com.cornershop.product.application.Product;
import com.cornershop.product.application.repository.ProductRepository;
import com.google.rpc.Code;
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
  public void listProduct(
      Service.ListProductRequest request, StreamObserver<Service.GetProductResponse> responseObserver) {

    log.info("Fetching products..");

    var productIds = request.getProductIdList();

    for (var productId : productIds) {
      log.info("Sending detail of product id {}", productId);
      // Fetch Product information from repository
      Optional<Product> optionalProduct = productRepository.get(productId);
      // If found send stream response else send error code

      Service.GetProductResponse getProductResponse;
      if (optionalProduct.isPresent()) {
        var product = optionalProduct.get();
        // If found build response
        var productResponse =
            Service.Product.newBuilder()
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .build();
        getProductResponse = Service.GetProductResponse.newBuilder().setProduct(productResponse).build();
        log.info("Called onNext for id {}", productId);
      } else {
        com.google.rpc.Status status =
            com.google.rpc.Status.newBuilder()
                .setCode(Code.NOT_FOUND.getNumber())
                .setMessage("Product id not found")
                .build();
        getProductResponse = Service.GetProductResponse.newBuilder().setError(status).build();
      }
      responseObserver.onNext(getProductResponse);
    }
    // Indicates that stream is done.
    responseObserver.onCompleted();
    log.info("Finished calling Product API service..");
  }
}
