package com.datntz.daprjavagrpc.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import com.datntz.daprjavagrpc.dto.EchoMessageDto;

@SpringBootTest
public class ResourceUtilsTest {
    @Test
    public void TestLoadJsonFile() throws Exception {
        var data = ResourceUtils.loadJson("echo.json", EchoMessageDto.class);
        Assert.notNull(data, "resource utils load json file error");
        Assert.hasText(data.getMessage(), "resource utils load json file missing message error");
    }
}
