package com.datntz.daprjavagrpc.topic;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dapr.v1.DaprAppCallbackProtos.TopicEventRequest;
import io.dapr.v1.DaprAppCallbackProtos.TopicEventResponse;
import io.dapr.v1.DaprAppCallbackProtos.TopicEventResponse.TopicEventResponseStatus;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseTopicSubscriber<TRequest extends Object> implements TopicSubscriber {

    public static final TopicEventResponse RESPONSE_DROP = TopicEventResponse
            .newBuilder()
            .setStatus(TopicEventResponseStatus.DROP)
            .build();

    public static final TopicEventResponse RESPONSE_RETRY = TopicEventResponse
            .newBuilder()
            .setStatus(TopicEventResponseStatus.RETRY)
            .build();

    public static final TopicEventResponse RESPONSE_SUCCESS = TopicEventResponse
            .newBuilder()
            .setStatus(TopicEventResponseStatus.SUCCESS)
            .build();

    private final Class<TRequest> tRequestClazz;

    public BaseTopicSubscriber(Class<TRequest> tRequestClazz) {
        this.tRequestClazz = tRequestClazz;
    }

    @Override
    public TopicEventResponse call(TopicEventRequest request) {
        return call(parseRequestData(request));
    }

    public abstract TopicEventResponse call(Optional<TRequest> request);

    public Optional<TRequest> parseRequestData(TopicEventRequest request) {
        try {
            var mapper = new ObjectMapper();
            var eventData = mapper.readValue(request.getData().toByteArray(), tRequestClazz);
            return Optional.of(eventData);
        } catch (Exception e) {
            log.error("handle topic event parse json error: {}", e.toString());
            e.printStackTrace();
        }

        return Optional.empty();
    }
}