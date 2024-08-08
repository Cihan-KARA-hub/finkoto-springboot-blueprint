package com.finkoto.ocppmockserver.ocpp.config;

import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.model.core.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Slf4j
@Configuration
public class ClientCoreProfileConfig {

    @Bean
    public ClientCoreEventHandler getCoreEventHandler() {
        return new ClientCoreEventHandler() {
            @Override
            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(ChangeAvailabilityRequest request) {
                log.info(request.toString());
                return new ChangeAvailabilityConfirmation(AvailabilityStatus.Accepted);
            }

            @Override
            public GetConfigurationConfirmation handleGetConfigurationRequest(GetConfigurationRequest request) {
                log.info(request.toString());
                return new GetConfigurationConfirmation();
            }

            @Override
            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(ChangeConfigurationRequest request) {
                log.info(request.toString());
                return new ChangeConfigurationConfirmation(ConfigurationStatus.Accepted);
            }

            @Override
            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest request) {
                log.info(request.toString());
                return new ClearCacheConfirmation(ClearCacheStatus.Accepted);
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(DataTransferRequest request) {
                log.info(request.toString());

                return new DataTransferConfirmation(DataTransferStatus.Accepted);
            }

            @Override
            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest request) {
                log.info(request.toString());

                return new RemoteStartTransactionConfirmation(RemoteStartStopStatus.Accepted);
            }

            @Override
            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest request) {
                log.info(request.toString());

                return new RemoteStopTransactionConfirmation(RemoteStartStopStatus.Accepted);
            }

            @Override
            public ResetConfirmation handleResetRequest(ResetRequest request) {
                log.info(request.toString());

                return new ResetConfirmation(ResetStatus.Accepted);
            }

            @Override
            public UnlockConnectorConfirmation handleUnlockConnectorRequest(UnlockConnectorRequest request) {
                log.info(request.toString());
                return new UnlockConnectorConfirmation(UnlockStatus.Unlocked);
            }
        };
    }

    @Bean
    public ClientCoreProfile createCore(ClientCoreEventHandler clientCoreEventHandler) {
        return new ClientCoreProfile(clientCoreEventHandler);
    }
}