package com.datntz.daprjavagrpc.client;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.google.protobuf.AbstractMessage;
import com.datntz.daprjavagrpc.utils.ProtobufUtils;

import io.dapr.client.domain.InvokeMethodRequest;
import io.dapr.utils.TypeRef;

@Component
public class DaprClient {

    private static volatile io.dapr.client.DaprClient instance;
    private static final TypeRef<byte[]> INVOKE_TYPEREF = new TypeRef<byte[]>() {

    };

    /**
     * Returns an DaprClient object.
     *
     * @return An DaprClient object.
     */
    public static io.dapr.client.DaprClient getInstance() {
        if (instance == null) {
            synchronized (io.dapr.client.DaprClient.class) {
                if (instance == null) {
                    instance = new io.dapr.client.DaprClientBuilder().build();
                }
            }
        }

        return instance;
    }

    /**
     * 
     * @param <TRequest>
     * @param <TResponse>
     * @param appId
     * @param method
     * @param requestData
     * @param responseClazz
     * @return
     */
    public <TRequest extends AbstractMessage, TResponse extends AbstractMessage> Optional<TResponse> invoke(
            String appId, String method,
            TRequest requestData,
            Class<TResponse> responseClazz) {
        var invokeMethodRequest = new InvokeMethodRequest(appId, method);
        invokeMethodRequest.setBody(ProtobufUtils.pack(requestData));
        var response = getInstance().invokeMethod(invokeMethodRequest, INVOKE_TYPEREF).block();
        if (response == null || response.length == 0) {
            return Optional.empty();
        }
        return ProtobufUtils.unpack(response, responseClazz);
    }
}
