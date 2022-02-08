package com.cornershop.api.product.application.error;

import com.cornershop.product.Resources;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.cornershop.commons.error.ErrorCode;
import com.cornershop.commons.error.ServiceException;
import io.grpc.StatusRuntimeException;

import java.util.Map;

/** Maps gRPC error to generic {@link ServiceException} */
public class ServiceExceptionMapper {

  public static ServiceException map(StatusRuntimeException error) {

    var status = io.grpc.protobuf.StatusProto.fromThrowable(error);

    Resources.ErrorDetail errorDetail = null;

    for (Any any : status.getDetailsList()) {
      if (!any.is(Resources.ErrorDetail.class)) {
        continue;
      }
      try {
        errorDetail = any.unpack(Resources.ErrorDetail.class);
      } catch (InvalidProtocolBufferException cause) {
        errorDetail =
            Resources.ErrorDetail.newBuilder()
                .setErrorCode(ErrorCode.INVALID_OPERATION.getMessage())
                .setMessage(cause.getMessage())
                .putAllMetadata(Map.of())
                .build();
      }
    }

    return new ServiceException(
        ErrorCode.errorCode(errorDetail.getErrorCode()),
        errorDetail.getMessage(),
        errorDetail.getMetadataMap());
  }
}
