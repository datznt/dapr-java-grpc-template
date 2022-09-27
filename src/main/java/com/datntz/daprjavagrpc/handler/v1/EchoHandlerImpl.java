package com.datntz.daprjavagrpc.handler.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.daprgrpcecho.grpc.v1.DaprGrpcEcho.EchoReply;
import com.daprgrpcecho.grpc.v1.DaprGrpcEcho.EchoRequest;
import com.datntz.daprjavagrpc.handler.InvokeMethodHandler;
import com.datntz.daprjavagrpc.service.EchoService;
import com.google.protobuf.AbstractMessage;
import io.dapr.v1.CommonProtos.InvokeRequest;
import io.grpc.Status;

@Component("v1.echo:msg")
public class EchoHandlerImpl implements InvokeMethodHandler {

    @Autowired
    protected EchoService echoService;

    @Override
    public AbstractMessage call(InvokeRequest request) {

        EchoRequest requestData;
        String message = null;

        try {
            requestData = EchoRequest.parseFrom(request.getData().getValue());

            // get value from request data
            message = requestData.getMessage();

            // check empty value
            assert StringUtils.hasText(message);

        } catch (Exception e) {
            throw Status.INVALID_ARGUMENT.withDescription("Echo invalid request data")
                    .asRuntimeException();
        }

        var reply = echoService.getMessage(message);

        if (reply.isEmpty()) {
            throw Status.INTERNAL.withDescription("Echo service internal error")
                    .asRuntimeException();
        }

        return EchoReply.newBuilder().setMessage(reply.get().getMessage()).build();
    }

}
