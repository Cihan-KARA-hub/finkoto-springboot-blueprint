package com.finkoto.identityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
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
