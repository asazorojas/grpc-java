package com.cornershop.product.api.interceptor;

import com.cornershop.product.Resources;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.cornershop.commons.error.ResourceNotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class ExceptionHandler {

  @GrpcExceptionHandler(ResourceNotFoundException.class)
  public StatusRuntimeException handleResourceNotFoundException(ResourceNotFoundException cause) {

    log.error("Error, message {}", cause.getMessage());

    Resources.ErrorDetail errorDetail =
        Resources.ErrorDetail.newBuilder()
            .setErrorCode(cause.getErrorCode().getShortCode())
            .setMessage(cause.getMessage())
            .putAllMetadata(cause.getErrorMetaData())
            .build();

    var status =
        com.google.rpc.Status.newBuilder()
            .setCode(Code.NOT_FOUND.getNumber())
            .setMessage("Resource not found")
            .addDetails(Any.pack(errorDetail))
            .build();

    return StatusProto.toStatusRuntimeException(status);
  }

  @GrpcExceptionHandler
  public Status handleInvalidArgument(IllegalArgumentException e) {
    return Status.INVALID_ARGUMENT.withDescription("Your description").withCause(e);
  }
}
