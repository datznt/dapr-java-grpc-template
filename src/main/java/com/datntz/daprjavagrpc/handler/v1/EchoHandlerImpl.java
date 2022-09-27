package com.datntz.daprjavagrpc.handler.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.daprgrpcecho.grpc.v1.DaprGrpcEcho.EchoReply;
import com.daprgrpcecho.grpc.v1.DaprGrpcEcho.EchoRequest;
import com.datntz.daprjavagrpc.handler.BaseInvokeMethodHandler;
import com.datntz.daprjavagrpc.service.EchoService;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Parser;

import io.grpc.Status;

@Component("v1.echo:msg")
public class EchoHandlerImpl extends BaseInvokeMethodHandler<EchoRequest> {

    @Autowired
    protected EchoService echoService;

    @Override
    public AbstractMessage call(EchoRequest requestData) {
        String message = requestData.getMessage();

        // check empty value
        if (!StringUtils.hasText(message)) {
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

    @Override
    public Parser<EchoRequest> getRequestParser() {
        return EchoRequest.parser();
    }

}
