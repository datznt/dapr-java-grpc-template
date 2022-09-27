package com.datntz.daprjavagrpc.handler.v1;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daprgrpcecho.grpc.v1.DaprGrpcEcho.EchoReply;
import com.daprgrpcecho.grpc.v1.DaprGrpcEcho.EchoRequest;
import com.datntz.daprjavagrpc.handler.InvokeMethodHandler;
import com.google.protobuf.Any;

import io.dapr.v1.CommonProtos.InvokeRequest;

@SpringBootTest
public class EchoHandlerITest {

    @Autowired
    Map<String, InvokeMethodHandler> handlers;

    @Test
    public void testRegisterMethod() {
        assertTrue(handlers.containsKey("v1.echo:msg"));
    }

    @Test
    public void testInvokeMethod() {
        var handler = handlers.get("v1.echo:msg");

        var echoRequest = EchoRequest.newBuilder().setMessage("hello").build();
        var invokeRequest = InvokeRequest.newBuilder().setData(Any.pack(echoRequest)).build();
        var invokeResponse = handler.call(invokeRequest);
        assertNotNull(invokeResponse);
        assertTrue(invokeResponse instanceof EchoReply);
    }
}
