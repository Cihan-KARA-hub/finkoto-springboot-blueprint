package com.finkoto.chargestation.ocpp;


import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.enums.SessionStatus;
import com.finkoto.chargestation.services.ChargePointService;
import com.finkoto.chargestation.services.ChargingSessionService;
import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.feature.profile.ServerCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ServerCoreProfile;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.SessionInformation;
import eu.chargetime.ocpp.model.core.*;
import eu.chargetime.ocpp.model.firmware.GetDiagnosticsRequest;
import eu.chargetime.ocpp.model.firmware.UpdateFirmwareRequest;
import eu.chargetime.ocpp.model.localauthlist.GetLocalListVersionRequest;
import eu.chargetime.ocpp.model.localauthlist.SendLocalListRequest;
import eu.chargetime.ocpp.model.localauthlist.UpdateType;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageRequest;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageRequestType;
import eu.chargetime.ocpp.model.reservation.CancelReservationRequest;
import eu.chargetime.ocpp.model.reservation.ReserveNowRequest;
import eu.chargetime.ocpp.model.securityext.*;
import eu.chargetime.ocpp.model.securityext.types.*;
import eu.chargetime.ocpp.model.smartcharging.SetChargingProfileRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletionStage;

@Slf4j
@Component
@RequiredArgsConstructor
public class OCPPCentralSystem implements ServerCoreEventHandler {
    private final Map<UUID, String> sessions = new HashMap<>();
    private ChargingSessionService chargingSessionService;
    private ChargePointService chargePointService;
    private JSONServer server;

    @Value("${websocket.port}")
    private Integer webSocketPort;

    @Value("${websocket.host}")
    private String webSocketHost;

    @Lazy
    @Autowired
    public void setChargingSessionService(ChargingSessionService chargingSessionService) {
        this.chargingSessionService = chargingSessionService;
    }

    @Lazy
    @Autowired
    public void setChargePointService(ChargePointService chargePointService) {
        this.chargePointService = chargePointService;
    }

    @PostConstruct
    public void startServer() {
        this.server = new JSONServer(new ServerCoreProfile(this));
        server.open(webSocketHost, webSocketPort, createServerEvents());
    }

    private ServerEvents createServerEvents() {
        return new ServerEvents() {

            @Override
            public void authenticateSession(SessionInformation sessionInformation, String s, byte[] bytes) {
            }

            @Override
            public void newSession(UUID sessionIndex, SessionInformation information) {
                final String chargePointOcppId = information.getIdentifier().substring(1);
                sessions.put(sessionIndex, chargePointOcppId);
                log.info("New session {}: {}", sessionIndex, information.getIdentifier());
                chargePointService.newSession(chargePointOcppId);
            }

            @Override
            public void lostSession(UUID sessionIndex) {
                sessions.remove(sessionIndex);
                log.info("Session {} lost connection...", sessionIndex);
                chargePointService.lostSession();

            }
        };
    }

    public String getOcppId(UUID sessionId) {
        return sessions.get(sessionId);
    }

    public UUID getSessionId(String ocppId) {
        return sessions.entrySet().stream().filter(entry
                -> entry.getValue().equals(ocppId)).findFirst().map(Map.Entry::getKey).orElseThrow(()
                -> new IllegalStateException("Can not find session with ocppId " + ocppId + "..."));
    }

    // TODO aşağıdaki handle ile başlayan tüm methodların içine slf4j ile info logu bastıralım.
    @Override
    public AuthorizeConfirmation handleAuthorizeRequest(UUID sessionIndex, AuthorizeRequest request) {
        IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted);
        idTagInfo.setExpiryDate(ZonedDateTime.now());
        idTagInfo.setParentIdTag("test");
        idTagInfo.setStatus(AuthorizationStatus.Accepted);
        return new AuthorizeConfirmation(idTagInfo);
    }

    @Override
    public BootNotificationConfirmation handleBootNotificationRequest(UUID sessionIndex, BootNotificationRequest request) {
        return new BootNotificationConfirmation(ZonedDateTime.now(), 0, RegistrationStatus.Accepted);
    }

    @Override
    public DataTransferConfirmation handleDataTransferRequest(UUID sessionIndex, DataTransferRequest request) {
        return new DataTransferConfirmation(DataTransferStatus.Accepted);
    }

    @Override
    public HeartbeatConfirmation handleHeartbeatRequest(UUID sessionIndex, HeartbeatRequest request) {
        chargePointService.handleHeartbeatRequest(getOcppId(sessionIndex));
        return new HeartbeatConfirmation(ZonedDateTime.now());
    }

    @Override
    public MeterValuesConfirmation handleMeterValuesRequest(UUID sessionIndex, MeterValuesRequest request) {
        Arrays.stream(request.getMeterValue()).findFirst().flatMap(meterValue -> Arrays.stream(meterValue.getSampledValue()).findFirst()).ifPresent(sampledValue -> {
            String value = sampledValue.getValue();
            chargingSessionService.handleMeterValuesRequest(value);
        });
        return new MeterValuesConfirmation();
    }

    @Override
    public StartTransactionConfirmation handleStartTransactionRequest(UUID sessionIndex, StartTransactionRequest request) {
        log.info(request.toString());
        final Optional<ChargingSession> optional = chargingSessionService.findNewChargingSession(getOcppId(sessionIndex), request.getConnectorId(), request.getIdTag(), SessionStatus.NEW);
        if (optional.isEmpty()) {
            return new StartTransactionConfirmation(new IdTagInfo(AuthorizationStatus.Invalid), 0);
        } else {
            final ChargingSession chargingSession = optional.get();
            chargingSessionService.activateNewChargingSession(chargingSession.getId(), request.getMeterStart());
            final IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted);
            return new StartTransactionConfirmation(idTagInfo, Math.toIntExact(chargingSession.getId()));
        }
    }

    @Override
    public StatusNotificationConfirmation handleStatusNotificationRequest(UUID sessionIndex, StatusNotificationRequest request) {
        // TODO connector tablosundaki status alanını güncelleyelim.
        return new StatusNotificationConfirmation();
    }

    @Override
    public StopTransactionConfirmation handleStopTransactionRequest(UUID sessionIndex, StopTransactionRequest request) {
        request.getTransactionId();
        final Optional<ChargingSession> optional = chargingSessionService.findById(request.getTransactionId());
        if (optional.isEmpty()) {
            new IdTagInfo(AuthorizationStatus.Invalid);
            return new StopTransactionConfirmation();
        }
        chargingSessionService.handleStopTransactionRequest(request.getTransactionId());
        final IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted);
        StopTransactionConfirmation confirmation = new StopTransactionConfirmation();
        confirmation.setIdTagInfo(idTagInfo);
        return confirmation;
    }

    public void sendChangeAvailabilityRequest(String ocppId, int connectorId, AvailabilityType type) {
        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest(connectorId, type);
        request.setType(type);
        request.setConnectorId(connectorId);
        send(ocppId, request);
    }

    public void sendChangeConfigurationRequest(String ocppId, String key, String value) {
        ChangeConfigurationRequest request = new ChangeConfigurationRequest(key, value);
        request.setKey(key);
        request.setValue(value);
        send(ocppId, request);
    }

    public void sendClearCacheRequest(String ocppId) {
        ClearCacheRequest request = new ClearCacheRequest();
        send(ocppId, request);
    }

    public void sendDataTransferRequest(String ocppId, String vendorId, String messageId, String data) {
        DataTransferRequest request = new DataTransferRequest(vendorId);
        request.setVendorId(vendorId);
        request.setMessageId(messageId);
        request.setData(data);
        send(ocppId, request);
    }

    public void sendGetConfigurationRequest(String ocppId, String... key) {
        GetConfigurationRequest request = new GetConfigurationRequest();
        request.setKey(key);
        send(ocppId, request);
    }

    public void sendRemoteStartTransactionRequest(String ocppId, int connectorId, String idTag) {
        boolean check = chargingSessionService.checkActiveSession(ocppId, connectorId, SessionStatus.ACTIVE);
        if (check) {
            // TODO bu kontrolü servis üzerinde yapalım
            // TODO ChargePointNotAvailable runtime exception oluşturup fırlatalım,
            // TODO @RestControllerAdvice ile burada fırlatılan exception yakalanıp 409 response code dönelim.
            log.error("Remote start for charging point failed charging point active");
        } else {
            // TODO charging_session tablosundan id_tag kolonunu kaldıralım.
            // TODO charging_session tablosunun id alanını idTag alanında yollayalım.
            // TODO charging_session.id = mock_charging_session.id_tag iki tablo arasındaki ilişki bu şekilde olacak.
            chargingSessionService.newChargingSession(ocppId, connectorId, idTag);
            final RemoteStartTransactionRequest request = new RemoteStartTransactionRequest(idTag);
            // TODO gereksiz setter'ları silelim.
            request.setConnectorId(connectorId);
            request.setIdTag(idTag);
            request.setConnectorId(connectorId);
            request.setIdTag(idTag);
            request.setConnectorId(connectorId);
            send(ocppId, request).whenComplete((confirmation, throwable) -> {
                if (throwable != null) {
                    log.error("Remote start transaction failed for charge point: {}", ocppId, throwable);
                } else {
                    log.info("Remote start transaction successful for charge point: {}", ocppId);
                }
            });
        }
    }


    public void sendRemoteStartTransactionWithProfileRequest(String ocppId, int connectorId, String idTag) {
        final RemoteStartTransactionRequest request = new RemoteStartTransactionRequest(idTag);
        request.setConnectorId(connectorId);

        ChargingSchedule schedule =
                new ChargingSchedule(
                        ChargingRateUnitType.A,
                        new ChargingSchedulePeriod[]{new ChargingSchedulePeriod(1, 32d)});
        ChargingProfile profile =
                new ChargingProfile(
                        1,
                        1,
                        ChargingProfilePurposeType.ChargePointMaxProfile,
                        ChargingProfileKindType.Recurring,
                        schedule);
        request.setChargingProfile(profile);
        send(ocppId, request);
    }

    //--------------------- ilk burası
    public void sendRemoteStopTransactionRequest(String ocppId, String idTag) {
        // TODO bu id charging_session tablosunun id si olacak.
        int id = Integer.parseInt(idTag);

        final RemoteStopTransactionRequest request = new RemoteStopTransactionRequest(id);

        send(ocppId, request).whenComplete((confirmation, throwable) -> {
            if (throwable != null) {
                //NULL GELDİ
                log.error("Remote stop transaction failed for charge point: {}", ocppId, throwable);
            } else {
                log.info("Remote stop transaction successful for charge point: {}", ocppId);
            }
        });
    }

    public void sendResetRequest(String ocppId, ResetType type) {
        ResetRequest request = new ResetRequest(type);
        request.setType(type);
        send(ocppId, request);
    }

    public void sendGetDiagnosticsRequest(String ocppId, String location) {
        GetDiagnosticsRequest request = new GetDiagnosticsRequest(location);
        request.setLocation(location);
        send(ocppId, request);
    }

    public void sendUpdateFirmwareRequest(String ocppId, String location, ZonedDateTime retrieveDate) {
        UpdateFirmwareRequest request = new UpdateFirmwareRequest(location, retrieveDate);
        send(ocppId, request);
    }

    public void sendReserveNowRequest(
            String ocppId, Integer connectorId, ZonedDateTime expiryDate, String idTag, Integer reservationId) {
        ReserveNowRequest request =
                new ReserveNowRequest(connectorId, expiryDate, idTag, reservationId);
        send(ocppId, request);
    }

    public void sendCancelReservationRequest(String ocppId, Integer reservationId) {
        CancelReservationRequest request = new CancelReservationRequest(reservationId);
        send(ocppId, request);
    }

    public void sendGetLocalListVersionRequest(String ocppId) {
        GetLocalListVersionRequest request = new GetLocalListVersionRequest();
        send(ocppId, request);
    }

    public void sendSendLocalListRequest(String ocppId, int listVersion, UpdateType updateType) {
        SendLocalListRequest request = new SendLocalListRequest(listVersion, updateType);
        send(ocppId, request);
    }

    public void sendSetChargingProfileRequest(String ocppId, Integer connectorId, ChargingProfile csChargingProfiles) {
        SetChargingProfileRequest request =
                new SetChargingProfileRequest(connectorId, csChargingProfiles);
        send(ocppId, request);
    }

    public void sendUnlockConnectorRequest(String ocppId, int connectorId) {
        UnlockConnectorRequest request = new UnlockConnectorRequest(connectorId);
        request.setConnectorId(connectorId);
        send(ocppId, request);
    }

    public void sendCertificateSignedRequest(String ocppId, String certificateChain) {
        Request request = new CertificateSignedRequest(certificateChain);
        send(ocppId, request);
    }

    public void sendDeleteCertificateRequest(String ocppId, CertificateHashDataType certificateHashDataType) {
        Request request = new DeleteCertificateRequest(certificateHashDataType);
        send(ocppId, request);
    }


    public void sendExtendedTriggerMessageRequest(String ocppId, MessageTriggerEnumType requestedMessage) {
        Request request = new ExtendedTriggerMessageRequest(requestedMessage);
        send(ocppId, request);
    }


    public void sendGetInstalledCertificateIdsRequest(String ocppId, CertificateUseEnumType certificateType) {
        Request request = new GetInstalledCertificateIdsRequest(certificateType);
        send(ocppId, request);
    }


    public void sendGetLogRequest(String ocppId, LogEnumType logType, Integer requestId, LogParametersType log) {
        Request request = new GetLogRequest(logType, requestId, log);
        send(ocppId, request);
    }


    public void sendInstallCertificateRequest(String ocppId, CertificateUseEnumType certificateType, String certificate) {
        Request request = new InstallCertificateRequest(certificateType, certificate);
        send(ocppId, request);
    }

    public void sendSignedUpdateFirmwareRequest(String ocppId, Integer requestId, FirmwareType firmware) {
        Request request = new SignedUpdateFirmwareRequest(requestId, firmware);
        send(ocppId, request);
    }


    public void sendTriggerMessage(String ocppId, TriggerMessageRequestType type, Integer connectorId) {
        TriggerMessageRequest request = new TriggerMessageRequest(type);
        request.setConnectorId(connectorId);
        send(ocppId, request);
    }

    private CompletionStage<Confirmation> send(String ocppId, Request request) {
        final UUID sessionId = getSessionId(ocppId);
        try {
            return server.send(sessionId, request);
        } catch (Exception e) {
            log.error("Can not send request to charge point: {}", request.toString(), e);
            throw new RuntimeException("Can not send request to charge point with ocppId: %s".formatted(ocppId), e);
        }
    }

}
