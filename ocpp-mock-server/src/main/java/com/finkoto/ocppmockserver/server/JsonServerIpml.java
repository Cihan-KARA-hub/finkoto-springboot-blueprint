package com.finkoto.ocppmockserver.server;


import com.finkoto.ocppmockserver.config.ApplicationConfiguration;
import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.ServerEvents;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class JsonServerIpml {
    private  final ServerEvents serverEvents;
    private  final JSONServer server;
    private final ApplicationConfiguration applicationConfiguration;

    @PostConstruct
    public void startServer() throws Exception {
        server.open("localhost", applicationConfiguration.getServerPort(), serverEvents);
    }

}
