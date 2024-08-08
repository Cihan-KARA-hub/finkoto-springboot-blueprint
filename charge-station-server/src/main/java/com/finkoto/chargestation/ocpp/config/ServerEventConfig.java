package com.finkoto.chargestation.ocpp.config;

import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.services.ChargePointService;
import eu.chargetime.ocpp.AuthenticationException;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.model.SessionInformation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@Getter
@Slf4j
public class ServerEventConfig {

    private final Map<UUID, String> sessions = new HashMap<>();
    private final ChargePointService chargePointService ;

    public ServerEventConfig(ChargePointService chargePointService) {
        this.chargePointService = chargePointService;
    }

    @Bean
    public ServerEvents createServerCoreImpl() {
        return getNewServerEventsImpl();
    }

    private ServerEvents getNewServerEventsImpl() {
        return new ServerEvents() {

            @Override
            public void authenticateSession(SessionInformation sessionInformation, String s, byte[] bytes) throws AuthenticationException {
                String s1 = sessions.get(sessionInformation);
                System.out.println(s1);

            }

            @Override
            public void newSession(UUID sessionIndex, SessionInformation information) {
                String chargePointOcppId = information.getIdentifier().substring(1);
                sessions.put(sessionIndex, chargePointOcppId);
                System.out.println("New session " + sessionIndex + ": " + information.getIdentifier());
                System.out.println("Adress session " + sessionIndex + ": " + information.getAddress());
                // TODO yeni balantı geldiginde chargePoint online : true ,lastConnected : now ,lastdisconnected : null yazilacak
                chargePointService.chargePointOnlineSet(chargePointOcppId);
            }

            @Override
            public void lostSession(UUID sessionIndex) {
                sessions.remove(sessionIndex);
                System.out.println("Session " + sessionIndex + " lost connection");
                // TODO yeni balantı geldiginde chargePoint online false yailacak
               chargePointService.findByOnlineReset();

            }
        };
    }
}