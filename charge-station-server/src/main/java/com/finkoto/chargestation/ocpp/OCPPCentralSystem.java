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

    @Override
    public AuthorizeConfirmation handleAuthorizeRequest(UUID sessionIndex, AuthorizeRequest request) {
        System.out.println(request);
        // ... handle event
        IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted);
        idTagInfo.setExpiryDate(ZonedDateTime.now());
        idTagInfo.setParentIdTag("test");
        idTagInfo.setStatus(AuthorizationStatus.Accepted);

        return new AuthorizeConfirmation(idTagInfo);
    }

    @Override
    public BootNotificationConfirmation handleBootNotificationRequest(UUID sessionIndex, BootNotificationRequest request) {
        System.out.println(request);
        BootNotificationConfirmation confirmation = new BootNotificationConfirmation(ZonedDateTime.now(), 0, RegistrationStatus.Accepted);
        System.out.println(request.getChargePointModel() + " \n" +
                request.getChargePointSerialNumber() + "  \n" +
                request.getMeterType() + " \n" +
                request.getImsi() + " \n" +
                request.getIccid() + " \n" +
                request.getFirmwareVersion() + " \n" +
                request.getMeterSerialNumber() + " \n" +
                request.getChargePointVendor() + " \n");

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
        chargePointService.handleHeartbeatRequest(getOcppId(sessionIndex));
        return new HeartbeatConfirmation(ZonedDateTime.now()); // returning null means unsupported feature
    }

    @Override
    public MeterValuesConfirmation handleMeterValuesRequest(UUID sessionIndex, MeterValuesRequest request) {
        Arrays.stream(request.getMeterValue()).findFirst().flatMap(meterValue -> Arrays.stream(meterValue.getSampledValue()).findFirst()).ifPresent(sampledValue -> {
            String value = sampledValue.getValue();
            chargingSessionService.handleMeterValuesRequest(value);
            System.out.println(value + "meter value");

        });
        System.out.println(request);
        return new MeterValuesConfirmation(); // returning null means unsupported feature
    }

    @Override
    public StartTransactionConfirmation handleStartTransactionRequest(UUID sessionIndex, StartTransactionRequest request) {
        log.info(request.toString());
        List<ChargingSession> sessionList = chargingSessionService.findByStatus(SessionStatus.NEW);
        for (ChargingSession session : sessionList) {
            ChargingSession sessionInd = chargingSessionService.activateNewChargingSession(session.getId(), session.getConnectorId());
            new StartTransactionConfirmation(new IdTagInfo(AuthorizationStatus.Accepted), Math.toIntExact(sessionInd.getId()));
        }
        return handleStartTransactionRequest(sessionIndex, request);
    }

    @Override
    public StatusNotificationConfirmation handleStatusNotificationRequest(UUID sessionIndex, StatusNotificationRequest request) {
        System.out.println(request);
        // ... handle event
        return new StatusNotificationConfirmation(); // returning null means unsupported feature
    }

    @Override
    public StopTransactionConfirmation handleStopTransactionRequest(UUID sessionIndex, StopTransactionRequest request) {
        System.out.println(request);
        chargingSessionService.remoteStop(request.getIdTag(), request.getTransactionId());
        return new StopTransactionConfirmation();
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
        chargingSessionService.newChargingSession(ocppId, connectorId, idTag);
        final RemoteStartTransactionRequest request = new RemoteStartTransactionRequest(idTag);
        request.setConnectorId(connectorId);
        send(ocppId, request).whenComplete((confirmation, throwable) -> {
            if (throwable != null) {
                log.error("Remote start transaction failed for charge point: {}", ocppId, throwable);
            } else {
                log.info("Remote start transaction successful for charge point: {}", ocppId);
            }
        });
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

    public void sendRemoteStopTransactionRequest(String ocppId, String idTag) {
        int id = Integer.parseInt(idTag);
        final RemoteStopTransactionRequest request = new RemoteStopTransactionRequest(id);
        send(ocppId, request).whenComplete((confirmation, throwable) -> {
            if (throwable != null) {
                log.error("Remote start transaction failed for charge point: {}", ocppId, throwable);
            } else {
                log.info("Remote start transaction successful for charge point: {}", ocppId);
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
