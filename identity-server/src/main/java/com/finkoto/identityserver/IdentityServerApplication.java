package com.finkoto.identityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class IdentityServerApplication  {

    public static void main(String[] args) {
       try {
           SpringApplication.run(IdentityServerApplication.class, args);
       }
       catch (Exception e) {
           e.printStackTrace();
       }
    }

}
