package com.cornershop.order.client;

import com.cornershop.product.api.ProductServiceGrpc;
import com.cornershop.product.api.Service;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannelBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UnaryGrpcFutureClient {

  private final String host;
  private final int port;
  private final ExecutorService fixedThreadPool;

  public UnaryGrpcFutureClient(String host, int port) {
    this.host = host;
    this.port = port;
    this.fixedThreadPool = Executors.newFixedThreadPool(2);
  }

  @SneakyThrows
  public void callServer() {

    log.info("Calling Server..");
    var managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    // Create a new future stub
    var productServiceFutureStub = ProductServiceGrpc.newFutureStub(managedChannel);

    var productRequest = Service.GetProductRequest.newBuilder().setProductId("apple-123").build();

    ListenableFuture<Service.GetProductResponse> listenableFuture =
        productServiceFutureStub.getProduct(productRequest);

    listenableFuture.addListener(this::notifyListener, fixedThreadPool);

    Futures.addCallback(listenableFuture, new ProductCallback(), fixedThreadPool);

    Thread.sleep(2000);

    fixedThreadPool.shutdown();
    fixedThreadPool.awaitTermination(2, TimeUnit.SECONDS);
  }

  private void notifyListener() {
    log.info("Notifying downstream operation");
  }

  private static class ProductCallback implements FutureCallback<Service.GetProductResponse> {

    @Override
    public void onSuccess(@NullableDecl Service.GetProductResponse result) {
      log.info("Received product {}", result);
    }

    @Override
    public void onFailure(Throwable error) {
      log.error("Error occurred, reason {}", error.getMessage());
    }
  }

  public static void main(String[] args) {
    var client = new UnaryGrpcFutureClient("0.0.0.0", 3000);
    client.callServer();
  }
}
