package com.datntz.daprjavagrpc.handler;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;

import io.dapr.v1.CommonProtos.InvokeRequest;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseInvokeMethodHandler<TRequest extends Message>
        implements InvokeMethodHandler {

    private static final RuntimeException INVALID_MESSAGE = Status.INVALID_ARGUMENT
            .withDescription("Invalid message parsed type")
            .asRuntimeException();

    /**
     * 
     * @param <TRequest> message incoming data
     * @return
     */
    public abstract AbstractMessage call(TRequest requestData);

    /**
     * 
     * @return
     */
    public abstract Parser<TRequest> getRequestParser();

    @Override
    public AbstractMessage call(InvokeRequest request) {
        TRequest requestData = null;
        try {
            requestData = getRequestParser().parseFrom(request.getData().getValue());
        } catch (Exception e) {
            log.error("method handler parse message error: {}", e.toString());
            e.printStackTrace();

            throw INVALID_MESSAGE;
        }

        return call(requestData);
    }
}
