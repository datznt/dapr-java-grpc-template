package com.datntz.daprjavagrpc.handler;

import com.google.protobuf.AbstractMessage;

import io.dapr.v1.CommonProtos.InvokeRequest;

public interface InvokeMethodHandler {
    AbstractMessage call(InvokeRequest request);
}
