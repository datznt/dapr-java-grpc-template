package com.datntz.daprjavagrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan({ "com.qulot.daprjavagrpc", })
public class DaprJavaTemplateApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DaprJavaTemplateApplication.class, args);
	}
}
