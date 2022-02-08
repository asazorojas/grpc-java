package com.cornershop.order.client;

import com.cornershop.product.api.ProductServiceGrpc;
import com.cornershop.product.api.Service;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnaryGrpcAsyncClient {

  private final String host;
  private final int port;

  public UnaryGrpcAsyncClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @SneakyThrows
  public void callServer() {

    log.info("Calling Server..");
    var managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    // Create a new async stub
    var productServiceAsyncStub = ProductServiceGrpc.newStub(managedChannel);

    var productRequest = Service.GetProductRequest.newBuilder().setProductId("apple-123").build();

    productServiceAsyncStub.getProduct(productRequest, new ProductCallback());

    Thread.sleep(3000);

    log.info("Finished call");
  }

  private static class ProductCallback implements StreamObserver<Service.GetProductResponse> {

    @Override
    public void onNext(Service.GetProductResponse value) {
      log.info("Received product, {}", value);
    }

    @Override
    public void onError(Throwable cause) {
      log.error("Error occurred, cause {}", cause.getMessage());
    }

    @Override
    public void onCompleted() {
      log.info("Stream completed");
    }
  }

  public static void main(String[] args) {
    var client = new UnaryGrpcAsyncClient("0.0.0.0", 3000);
    client.callServer();
  }
}
