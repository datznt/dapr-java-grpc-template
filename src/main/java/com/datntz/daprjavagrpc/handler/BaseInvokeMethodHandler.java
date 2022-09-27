package com.datntz.daprjavagrpc.handler;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.ByteString;

import io.dapr.v1.CommonProtos.InvokeRequest;
import io.grpc.Status;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseInvokeMethodHandler<TRequest extends AbstractMessage> implements InvokeMethodHandler {

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
     * get message incoming type
     * 
     * @return
     */
    public abstract Class<TRequest> getTypeRequest();

    /**
     * 
     * @param data
     * @param clazz
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private TRequest unpack(ByteString data) throws Exception {
        var parseFrom = getTypeRequest().getMethod("parseFrom", ByteString.class);
        TRequest responseObj = (TRequest) parseFrom.invoke(null, data);
        return responseObj;
    }

    @Override
    public AbstractMessage call(InvokeRequest request) {
        TRequest requestData = null;
        try {
            requestData = unpack(request.getData().getValue());
        } catch (Exception e) {
            log.error("method handler parse message error: {}", e.toString());
            e.printStackTrace();

            throw INVALID_MESSAGE;
        }

        return call(requestData);
    }
}
