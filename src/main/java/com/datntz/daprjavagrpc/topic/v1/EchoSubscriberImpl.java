package com.datntz.daprjavagrpc.topic.v1;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.datntz.daprjavagrpc.topic.BaseTopicSubscriber;

import io.dapr.v1.DaprAppCallbackProtos.TopicEventResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("v1.daprjavagrpc:echo")
public class EchoSubscriberImpl
        extends BaseTopicSubscriber<EchoSubscriberImpl.EchoEventData> {

    private static final String PUBSUB_NAME = "daprjavagrpc";

    public EchoSubscriberImpl() {
        super(EchoEventData.class);
    }

    @Override
    public String getPubsubName() {
        return PUBSUB_NAME;
    }

    @Override
    public TopicEventResponse call(Optional<EchoEventData> request) {
        if (request.isEmpty()) {
            return RESPONSE_DROP;
        }

        log.info("topic v1.daprjavagrpc:echo receive message: {}", request.get().message);
        return RESPONSE_SUCCESS;
    }

    public static class EchoEventData {
        public String message;
    }
}
