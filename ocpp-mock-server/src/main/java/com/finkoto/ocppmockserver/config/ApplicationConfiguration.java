package com.finkoto.ocppmockserver.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@Getter
public class ApplicationConfiguration {
    @Value("8443")
    private Integer serverPort;
}
