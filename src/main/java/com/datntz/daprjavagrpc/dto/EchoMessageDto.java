package com.datntz.daprjavagrpc.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class EchoMessageDto implements Serializable {
    private String message;
}
