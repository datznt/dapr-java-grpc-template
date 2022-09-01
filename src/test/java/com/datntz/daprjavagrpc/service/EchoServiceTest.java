package com.datntz.daprjavagrpc.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EchoServiceTest {
    @Autowired
    EchoService echoService;

    @Test
    public void testGetMessage() {
        var msg = echoService.getMessage("daprjavagrpc");
        Assert.assertTrue(msg.isPresent());
        Assert.assertEquals("hello, client echo: daprjavagrpc", msg.get().getMessage());
    }
}
