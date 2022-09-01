package com.datntz.daprjavagrpc.service;

import java.util.Optional;
import com.datntz.daprjavagrpc.dto.EchoMessageDto;

public interface EchoService {

    /**
     * 
     * 
     * @param symbol
     * @param network
     * @return
     */
    Optional<EchoMessageDto> getMessage(String message);

}