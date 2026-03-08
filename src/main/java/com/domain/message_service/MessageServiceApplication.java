package com.domain.message_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = {
        "com.domain.message_service",
        "com.domain.cryptography",
        "com.domain.authcommon",
        "com.domain.exception",
        "com.domain.mapper",
})
public class MessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }

}
