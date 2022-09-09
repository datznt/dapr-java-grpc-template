package com.datntz.daprjavagrpc.utils;

import java.util.Optional;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProtobufUtils {
    /**
     * 
     * @param <T>
     * @param message
     * @return
     */
    public static <T extends Message> byte[] pack(T message) {
        return Any.pack(message).toByteArray();
    }

    /**
     * 
     * @param <T>
     * @param bytes
     * @param clazz
     * @return
     */
    public static <T extends Message> Optional<T> unpack(byte[] bytes, Class<T> clazz) {
        try {
            return Optional.of(Any.parseFrom(bytes).unpack(clazz));
        } catch (InvalidProtocolBufferException e) {
            log.error("protobuf utils unpack bytes error: {}", e.toString());
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
