package com.cornershop.order.client;

import com.cornershop.product.api.ProductServiceGrpc;
import com.cornershop.product.api.Service;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnaryGrpcBlockingClient {

  private final String host;
  private final int port;

  public UnaryGrpcBlockingClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void callServer() {

    log.info("Calling Server..");
    var managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    var productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(managedChannel);

    var productRequest = Service.GetProductRequest.newBuilder().setProductId("apple-123").build();

    var productResponse = productServiceBlockingStub.getProduct(productRequest);

    log.info("Received Product from server, info {}", productResponse);
  }

  public static void main(String[] args) {
    var client = new UnaryGrpcBlockingClient("0.0.0.0", 3000);
    client.callServer();
  }
}
