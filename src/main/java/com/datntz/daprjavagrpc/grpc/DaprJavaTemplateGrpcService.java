package com.datntz.daprjavagrpc.grpc;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import com.datntz.daprjavagrpc.handler.InvokeMethodHandler;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Any;
import io.dapr.v1.AppCallbackGrpc.AppCallbackImplBase;
import io.dapr.v1.CommonProtos.InvokeRequest;
import io.dapr.v1.CommonProtos.InvokeResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@Log4j2
@GrpcService
public class DaprJavaTemplateGrpcService extends AppCallbackImplBase {

    @Autowired
    protected Map<String, InvokeMethodHandler> handlers;

    @PostConstruct
    protected void init() {
        for (String method : handlers.keySet()) {
            log.info("grpc method ==> {}", method);
        }
    }

    @Override
    public void onInvoke(InvokeRequest request, StreamObserver<InvokeResponse> responseObserver) {
        AbstractMessage data = null;

        try {
            // invoke method by request method and request data
            data = handlers.get(request.getMethod()).call(request);
        } catch (StatusRuntimeException e) {

            // mark request as error
            responseObserver.onError(e);
            return;
        } catch (Exception e) {
            log.error("method: {}, error: {}", request.getMethod(), e.toString());
            e.printStackTrace();

            // build response has unknown exception
            var serviceGrpcError = Status.INTERNAL
                    .withDescription(String.format("Internal service error: %s", e.toString()))
                    .asException();

            // mark request as error
            responseObserver.onError(serviceGrpcError);
            return;
        }

        if (data != null) {

            // build response has data 
            responseObserver.onNext(InvokeResponse
                    .newBuilder()
                    .setData(Any.pack(data))
                    .build());
        }

        // mark request as completed
        responseObserver.onCompleted();
    }
}