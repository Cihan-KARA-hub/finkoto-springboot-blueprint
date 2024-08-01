package com.finkoto.ocppmockserver.config;


import eu.chargetime.ocpp.AuthenticationException;
import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.feature.profile.ServerCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ServerCoreProfile;
import eu.chargetime.ocpp.model.SessionInformation;
import eu.chargetime.ocpp.model.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Slf4j
public class JsonServerConfig {

    @Bean
    public JSONServer jsonServer(ServerCoreProfile coreProfile) {
        return new JSONServer(coreProfile);
    }
    private JSONServer server;
    private ServerCoreProfile core;

    public void started() throws Exception
    {
        if (server != null)
            return;

        // The core profile is mandatory
        core = new ServerCoreProfile(new ServerCoreEventHandler() {
            @Override
            public AuthorizeConfirmation handleAuthorizeRequest(UUID sessionIndex, AuthorizeRequest request) {

                System.out.println(request);
                // ... handle event

                return new AuthorizeConfirmation();
            }

            @Override
            public BootNotificationConfirmation handleBootNotificationRequest(UUID sessionIndex, BootNotificationRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(UUID sessionIndex, DataTransferRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public HeartbeatConfirmation handleHeartbeatRequest(UUID sessionIndex, HeartbeatRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public MeterValuesConfirmation handleMeterValuesRequest(UUID sessionIndex, MeterValuesRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public StartTransactionConfirmation handleStartTransactionRequest(UUID sessionIndex, StartTransactionRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public StatusNotificationConfirmation handleStatusNotificationRequest(UUID sessionIndex, StatusNotificationRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public StopTransactionConfirmation handleStopTransactionRequest(UUID sessionIndex, StopTransactionRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }
        });

        server = new JSONServer(core);
        server.open("localhost", 8887, new ServerEvents() {

            /**
             * @param sessionInformation
             * @param s
             * @param bytes
             * @throws AuthenticationException
             */
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
        });
    }

    public void sendClearCacheRequest() throws Exception {

        // Use the feature profile to help create event
        ClearCacheRequest request = core.createClearCacheRequest();

        UUID sessionIndex = null;
        // Server returns a promise which will be filled once it receives a confirmation.
        // Select the distination client with the sessionIndex integer.
        server.send(sessionIndex, request).whenComplete((confirmation, throwable) -> System.out.println(confirmation));
    }

}
