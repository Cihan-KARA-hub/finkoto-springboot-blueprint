package com.finkoto.ocppmockserver.ocpp.client;

import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.ConnectorStatus;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.server.FakeChargePoint;
import com.finkoto.ocppmockserver.services.MockChargePointServices;
import com.finkoto.ocppmockserver.services.MockChargingSessionServices;
import com.finkoto.ocppmockserver.services.MockConnectorServices;
import com.finkoto.ocppmockserver.services.OcppLoggerService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class JsonClientImpl {

    private final MockChargingSessionServices mockChargingSessionServices;
    private final MockConnectorServices mockConnectorServices;
    private final MockChargePointServices mockChargePointServices;
    private final OcppLoggerService ocppLoggerService;
    private final Map<String, FakeChargePoint> connections = new HashMap<>();
    private List<ChargePoint> chargePointsLostList = new ArrayList<>();

    @Value("${websocket.url}")
    private String webSocketUrl;

    public JsonClientImpl(MockChargingSessionServices mockChargingSessionServices, MockConnectorServices mockConnectorServices, MockChargePointServices mockChargePointServices, OcppLoggerService ocppLoggerService) {
        this.mockChargingSessionServices = mockChargingSessionServices;
        this.mockConnectorServices = mockConnectorServices;
        this.mockChargePointServices = mockChargePointServices;
        this.ocppLoggerService = ocppLoggerService;
    }

    @PostConstruct
    public void startServer() {
        mockChargePointServices.findAll().forEach(chargePoint -> {
            FakeChargePoint fakeChargePoint = new FakeChargePoint(chargePoint.getOcppId(), mockChargingSessionServices, mockConnectorServices, ocppLoggerService);
            fakeChargePoint.connect(chargePoint.getOcppId(), webSocketUrl);

            connections.put(chargePoint.getOcppId(), fakeChargePoint);
            fakeChargePoint.sendBootNotification(chargePoint.getChargeHardwareSpec());
        });
    }

    private Optional<FakeChargePoint> getFakeChargePoint(String ocppId) {
        return Optional.ofNullable(connections.get(ocppId));
    }

    @Scheduled(fixedRate = 10000)
    public void heatBeatScheduler() {
        List<ChargePoint> onlineChargePoints = mockChargePointServices.findByOnline(true);
        onlineChargePoints.forEach(chargePoint -> {
            FakeChargePoint fakeChargePoint = connections.get(chargePoint.getOcppId());
            if (fakeChargePoint != null) {
                fakeChargePoint.sendHeartbeatRequest();
            }
        });
    }

    @Scheduled(fixedRate = 10000)
    public void startTransactionScheduler() {
        List<MockChargingSession> sessions = mockChargingSessionServices.findByStatus(SessionStatus.NEW);
        for (MockChargingSession session : sessions) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                mockConnectorServices.updateStatus(session.getConnectorId(), session.getChargePointOcppId(), ConnectorStatus.Preparing);
                getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint ->
                        fakeChargePoint.sendStatusNotificationRequest(session.getConnectorId(), session.getChargePointOcppId()));
            }).thenRunAsync(() -> {
                mockChargingSessionServices.activateNewChargingSession(session.getId());
                getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint ->
                        fakeChargePoint.sendStartTransactionRequest(session.getConnectorId(), session.getIdTag(), session.getMeterStart()));
            }).thenRunAsync(() -> {
                mockConnectorServices.updateStatus(session.getConnectorId(), session.getChargePointOcppId(), ConnectorStatus.Charging);
                getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint ->
                        fakeChargePoint.sendStatusNotificationRequest(session.getConnectorId(), session.getChargePointOcppId()));
            }).exceptionally(ex -> {
                log.error("Error in startTransactionScheduler", ex);
                return null;
            });

        }
    }

    @Scheduled(fixedRate = 10000)
    public void stopTransactionScheduler() {
        List<MockChargingSession> sessions = mockChargingSessionServices.findByStatus(SessionStatus.FINISHING);
        for (MockChargingSession session : sessions) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                mockConnectorServices.updateStatus(session.getConnectorId(), session.getChargePointOcppId(), ConnectorStatus.Finishing);
                getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint ->
                        fakeChargePoint.sendStatusNotificationRequest(session.getConnectorId(), session.getChargePointOcppId()));
            }).thenRunAsync(() -> {
                mockChargingSessionServices.sendStopTransactionRequest(session.getId());
                getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint ->
                        fakeChargePoint.sendStopTransactionRequest(session.getMeterStop(), session.getId()));
            }).thenRunAsync(() -> {
                mockConnectorServices.updateStatus(session.getConnectorId(), session.getChargePointOcppId(), ConnectorStatus.Available);
                getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint ->
                        fakeChargePoint.sendStatusNotificationRequest(session.getConnectorId(), session.getChargePointOcppId()));
            });
        }
    }

    @Scheduled(fixedRate = 10000)
    public void meterValueScheduler() {
        List<MockChargingSession> sessions = mockChargingSessionServices.findByStatus(SessionStatus.ACTIVE);
        for (MockChargingSession session : sessions) {
            String meterValue = mockChargingSessionServices.updateMeterValue(session.getId());
            getFakeChargePoint(session.getChargePointOcppId())
                    .ifPresent(fakeChargePoint -> fakeChargePoint.sendMeterValuesRequest(session.getConnectorId(), meterValue, session.getIdTag()));
        }
    }


    @Scheduled(fixedRate = 50000)
    public void reconnetScheduler() {
        chargePointsLostList = mockChargePointServices.findByOnline(false);
        chargePointsLostList.forEach(chargePoint -> {
            log.info("reconnecting to " + chargePoint.getOcppId());
            startServer();
        });

    }
}
