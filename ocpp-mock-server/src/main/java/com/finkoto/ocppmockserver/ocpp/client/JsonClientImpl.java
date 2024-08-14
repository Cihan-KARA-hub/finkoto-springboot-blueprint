package com.finkoto.ocppmockserver.ocpp.client;

import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.server.FakeChargePoint;
import com.finkoto.ocppmockserver.services.ChargePointServices;
import com.finkoto.ocppmockserver.services.ConnectorServices;
import com.finkoto.ocppmockserver.services.MockChargingSessionServices;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component

public class JsonClientImpl {

    private final MockChargingSessionServices mockChargingSessionServices;
    private final ConnectorServices connectorServices;
    private final ChargePointServices chargePointService;
    private final Map<String, FakeChargePoint> connections = new HashMap<>();
    @Value("${websocket.url}")
    private String webSocketUrl;

    public JsonClientImpl(MockChargingSessionServices mockChargingSessionServices, ConnectorServices connectorServices, ChargePointServices chargePointService) {
        this.mockChargingSessionServices = mockChargingSessionServices;
        this.connectorServices = connectorServices;
        this.chargePointService = chargePointService;
    }

    @PostConstruct
    public void startServer() {
        chargePointService.findAll().forEach(chargePoint -> {
            FakeChargePoint fakeChargePoint = new FakeChargePoint(chargePoint.getOcppId(), mockChargingSessionServices, connectorServices);
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
        List<ChargePoint> onlineChargePoints = chargePointService.findByOnline(true);
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
            mockChargingSessionServices.activateNewChargingSession(session.getId());
            getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint -> fakeChargePoint.sendStartTransactionRequest(session.getConnectorId(), session.getIdTag(), session.getMeterStart()));
        }
    }

    @Scheduled(fixedRate = 10000)
    public void meterValueScheduler() {
        List<MockChargingSession> sessions = mockChargingSessionServices.findByStatus(SessionStatus.ACTIVE);
        for (MockChargingSession session : sessions) {
            String meterValue = mockChargingSessionServices.updateMeterValue(session.getId());
            getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint -> fakeChargePoint.sendMeterValuesRequest(session.getConnectorId(), meterValue));
        }
    }
}
