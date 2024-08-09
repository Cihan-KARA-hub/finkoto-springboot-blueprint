package com.finkoto.chargestation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EntityScan(basePackages = "com.finkoto.chargestation.model")
@EnableDiscoveryClient
public class ChargeStationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargeStationServerApplication.class, args);
    }

}
