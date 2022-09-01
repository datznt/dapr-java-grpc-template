package com.datntz.daprjavagrpc.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.datntz.daprjavagrpc.dto.EchoMessageDto;
import com.datntz.daprjavagrpc.utils.ResourceUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public Optional<EchoMessageDto> getMessage(String message) {
        try {
            var data = ResourceUtils.loadJson("echo.json", EchoMessageDto.class);
            var msgFormate = String.format(data.getMessage(), message);
            data.setMessage(msgFormate);
            return Optional.of(data);
        } catch (Exception e) {
            log.error("get message error: {}", e.toString());
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
