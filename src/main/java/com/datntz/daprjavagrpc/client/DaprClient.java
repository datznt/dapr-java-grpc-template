package com.datntz.daprjavagrpc.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.protobuf.AbstractMessage;

import io.dapr.client.domain.HttpExtension;
import io.dapr.client.domain.InvokeMethodRequest;
import io.dapr.client.domain.QueryStateItem;
import io.dapr.client.domain.QueryStateRequest;
import io.dapr.client.domain.SaveStateRequest;
import io.dapr.client.domain.State;
import io.dapr.client.domain.query.Query;
import io.dapr.utils.TypeRef;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DaprClient {

    private static volatile io.dapr.client.DaprClient instance;
    private static volatile io.dapr.client.DaprPreviewClient instancePreview;

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

    public static io.dapr.client.DaprPreviewClient getPreviewInstance() {
        if (instancePreview == null) {
            synchronized (io.dapr.client.DaprPreviewClient.class) {
                if (instancePreview == null) {
                    instancePreview = new io.dapr.client.DaprClientBuilder().buildPreviewClient();
                }
            }
        }

        return instancePreview;
    }

    /**
     * 
     * @param <T>
     * @param data
     * @param clazz
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected <T extends AbstractMessage> T unpack(byte[] data, Class<T> clazz) throws Exception {
        var parseFrom = clazz.getMethod("parseFrom", byte[].class);
        var responseObj = (T) parseFrom.invoke(null, data);
        return responseObj;
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
        invokeMethodRequest.setBody(requestData.toByteArray());
        invokeMethodRequest.setHttpExtension(HttpExtension.POST);

        try {
            var response = getInstance().invokeMethod(invokeMethodRequest, TypeRef.BYTE_ARRAY).block();
            if (response != null) {
                return Optional.of(unpack(response, responseClazz));
            }

        } catch (Exception e) {
            log.error("dapr client invoke request error: {}", e.toString());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    final Map<String, DaprState> states = new HashMap<>();

    /**
     * Return state management
     * 
     * 
     * @param name
     * @return
     */
    public DaprState stateOf(String name) {
        return states.containsKey(name)
                ? states.get(name)
                : new DaprState(name, getInstance(), getPreviewInstance());
    }

    public static class DaprState {
        private final String storeName;
        private final io.dapr.client.DaprClient client;
        private final io.dapr.client.DaprPreviewClient clientPreview;

        public DaprState(String storeName, io.dapr.client.DaprClient client,
                io.dapr.client.DaprPreviewClient clientPreview) {
            this.storeName = storeName;
            this.client = client;
            this.clientPreview = clientPreview;
        }

        /**
         * 
         * @param key
         * @param value
         * @return
         */
        public boolean set(String key, Object value) {

            try {
                client.saveState(storeName, key, value).block();
                return true;
            } catch (Exception e) {
                log.error("state {}, set key: {}, error: {}", storeName, key, e.toString());
                e.printStackTrace();
            }

            return false;
        }

        /**
         * 
         * @param key
         * @param states
         * @return
         */
        public boolean set(String key, State<?>... states) {

            try {
                var meta = new HashMap<>();
                meta.put("contentType", "application/json");
                var request = new SaveStateRequest(storeName).setStates(states);
                client.saveBulkState(request).block();
                return true;
            } catch (Exception e) {
                log.error("state {}, set key: {}, error: {}", storeName, key, e.toString());
                e.printStackTrace();
            }

            return false;
        }

        /**
         * 
         * @param <T>
         * @param key
         * @param clazz
         * @return
         */
        public <T> Optional<T> get(String key, Class<T> clazz) {
            try {
                var state = client.getState(storeName, key, clazz).block();
                return Optional.of(state.getValue());
            } catch (Exception e) {
                log.error("state {}, get key: {}, error: {}", storeName, key, e.toString());
                e.printStackTrace();
            }

            return Optional.empty();
        }

        /**
         * 
         * @param <T>
         * @param query
         * @param clazz
         * @return
         */
        public <T> List<T> get(Query query, Class<T> clazz) {
            try {
                var request = new QueryStateRequest(storeName)
                        .setQuery(query);

                var states = clientPreview.queryState(request, clazz).block();
                return states
                        .getResults()
                        .stream()
                        .map(QueryStateItem<T>::getValue)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                log.error("state {}, query: {}, error: {}", storeName, query.toString(), e.toString());
                e.printStackTrace();
            }

            return Collections.emptyList();
        }

        /**
         * 
         * @param key
         * @return
         */
        public boolean delete(String key) {
            try {
                client.deleteState(storeName, key).block();
                return true;
            } catch (Exception e) {
                log.error("state {}, delete key: {}, error: {}", storeName, key, e.toString());
                e.printStackTrace();
            }

            return false;
        }
    }
}
