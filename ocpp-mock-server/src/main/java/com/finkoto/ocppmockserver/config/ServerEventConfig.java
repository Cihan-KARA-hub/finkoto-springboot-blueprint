package com.finkoto.ocppmockserver.config;

import eu.chargetime.ocpp.AuthenticationException;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.model.SessionInformation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Getter
@Slf4j
public class ServerEventConfig  {
    @Bean
    public ServerEvents createServerCoreImpl() {
        return getNewServerEventsImpl();
    }
    private ServerEvents getNewServerEventsImpl() {
        ServerEvents serverEvents = new ServerEvents() {

            @Override
            public void authenticateSession(SessionInformation sessionInformation, String s, byte[] bytes) throws AuthenticationException {
            }

            @Override
            public void newSession(UUID sessionIndex, SessionInformation information) {
                // sessionIndex is used to send messages.
                System.out.println("New session " + sessionIndex + ": " + information.getIdentifier());
            }

            @Override
            public void lostSession(UUID sessionIndex) {
                System.out.println("Session " + sessionIndex + " lost connection");
            }
        };
        return serverEvents;
    }
}