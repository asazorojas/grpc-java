package com.cornershop.product.config;

import com.cornershop.product.api.interceptor.ExceptionHandler;
import com.cornershop.product.api.interceptor.LogInterceptor;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

  @GrpcGlobalServerInterceptor
  public LogInterceptor logServerInterceptor() {
    return new LogInterceptor();
  }

  @GrpcAdvice
  public ExceptionHandler exceptionHandler() {
    return new ExceptionHandler();
  }

//  @GrpcGlobalServerInterceptor
//  public GlobalExceptionHandlerInterceptor errorHandler() {
//    return new GlobalExceptionHandlerInterceptor();
//  }

}
