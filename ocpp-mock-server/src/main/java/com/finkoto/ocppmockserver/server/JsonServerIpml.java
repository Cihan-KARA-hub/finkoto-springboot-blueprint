package com.finkoto.ocppmockserver.server;


import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.feature.profile.ServerCoreProfile;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class JsonServerIpml {
    private  final ServerEvents serverEvents;
    private  final JSONServer server;
    private final ServerCoreProfile serverCoreProfile;


    @Value("${websocket.port}")
    private Integer webSocketPort;
    @Value("${websocket.host}")
    private String webSocketHost;

    @PostConstruct
    public void startServer() throws Exception {
        server.open(webSocketHost,webSocketPort, serverEvents);
    }

}
