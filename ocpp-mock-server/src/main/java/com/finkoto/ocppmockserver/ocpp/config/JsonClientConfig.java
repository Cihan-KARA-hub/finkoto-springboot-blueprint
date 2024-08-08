package com.finkoto.ocppmockserver.ocpp.config;


import eu.chargetime.ocpp.JSONClient;
import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.feature.profile.ServerCoreProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JsonClientConfig {

    @Bean
    public JSONClient jsonServer(ClientCoreProfile core) {
        return new JSONClient(core);
    }

}
