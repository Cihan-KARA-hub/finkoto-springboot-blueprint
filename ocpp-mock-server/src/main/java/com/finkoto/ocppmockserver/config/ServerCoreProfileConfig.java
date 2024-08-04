package com.finkoto.ocppmockserver.config;

import eu.chargetime.ocpp.feature.profile.ServerCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ServerCoreProfile;
import eu.chargetime.ocpp.model.core.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.util.UUID;

@Configuration
@Getter
@Slf4j
public class ServerCoreProfileConfig {

    @Bean
    public ServerCoreEventHandler getCoreEventHandler() {
        return new ServerCoreEventHandler() {
            @Override
            public AuthorizeConfirmation handleAuthorizeRequest(UUID sessionIndex, AuthorizeRequest request) {
                System.out.println(request);
                // ... handle event
                IdTagInfo idTagInfo = new IdTagInfo();
                idTagInfo.setExpiryDate(ZonedDateTime.now());
                idTagInfo.setParentIdTag("test");
                idTagInfo.setStatus(AuthorizationStatus.Accepted);

                return new AuthorizeConfirmation(idTagInfo);
            }
            @Override
            public BootNotificationConfirmation handleBootNotificationRequest(UUID sessionIndex, BootNotificationRequest request) {
                System.out.println(request);
                // ... handle event
                BootNotificationConfirmation confirmation = new BootNotificationConfirmation();
                confirmation.setCurrentTime(ZonedDateTime.now());
                confirmation.setStatus(RegistrationStatus.Accepted);
                System.out.println(  request.getChargePointModel()+" \n"+
                request.getChargePointSerialNumber()+"  \n"+
                request.getMeterType());
                System.out.println(confirmation);


                return confirmation; // returning null means unsupported feature
            }
            @Override
            public DataTransferConfirmation handleDataTransferRequest(UUID sessionIndex, DataTransferRequest request) {
                System.out.println(request);

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
                RemoteStartTransactionConfirmation remoteStartTransactionConfirmation = new RemoteStartTransactionConfirmation();
                ChargingProfile chargingProfile = new ChargingProfile();
                request.getIdTag();
                request.validate();
                remoteStartTransactionConfirmation.getStatus();
                return null;
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

        };
    }
    @Bean
    public ServerCoreProfile createCore(ServerCoreEventHandler serverCoreEventHandler) {
        return new ServerCoreProfile(serverCoreEventHandler);
    }
}