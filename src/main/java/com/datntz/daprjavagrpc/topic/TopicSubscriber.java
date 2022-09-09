package com.datntz.daprjavagrpc.topic;

import io.dapr.v1.DaprAppCallbackProtos.TopicEventRequest;
import io.dapr.v1.DaprAppCallbackProtos.TopicEventResponse;

public interface TopicSubscriber {

    /**
     * 
     * @return
     */
    String getPubsubName();

    /**
     * 
     * @param request
     * @return
     */
    TopicEventResponse call(TopicEventRequest request);
}