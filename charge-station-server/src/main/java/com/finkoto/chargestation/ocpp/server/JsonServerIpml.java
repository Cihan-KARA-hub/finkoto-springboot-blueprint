package com.finkoto.chargestation.ocpp.server;


import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.model.core.BootNotificationRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonServerIpml {

    private final ServerEvents serverEvents;
    private final JSONServer server;

    @Value("${websocket.port}")
    private Integer webSocketPort;
    @Value("${websocket.host}")
    private String webSocketHost;


    @PostConstruct
    public void startServer() throws Exception {
        server.open(webSocketHost, webSocketPort, serverEvents);
    }
}
