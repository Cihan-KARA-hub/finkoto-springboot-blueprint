package com.finkoto.ocppmockserver.ocpp.client;

import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.server.FakeChargePoint;
import com.finkoto.ocppmockserver.services.MockChargePointServices;
import com.finkoto.ocppmockserver.services.MockChargingSessionServices;
import com.finkoto.ocppmockserver.services.MockConnectorServices;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class JsonClientImpl {

    private final MockChargingSessionServices mockChargingSessionServices;
    private final MockConnectorServices mockConnectorServices;
    private final MockChargePointServices mockChargePointServices;
    private final Map<String, FakeChargePoint> connections = new HashMap<>();
    private List<ChargePoint> chargePointsLostList = new ArrayList<>();

    @Value("${websocket.url}")
    private String webSocketUrl;

    public JsonClientImpl(MockChargingSessionServices mockChargingSessionServices, MockConnectorServices mockConnectorServices, MockChargePointServices mockChargePointServices) {
        this.mockChargingSessionServices = mockChargingSessionServices;
        this.mockConnectorServices = mockConnectorServices;
        this.mockChargePointServices = mockChargePointServices;
    }

    @PostConstruct
    public void startServer() {
        mockChargePointServices.findAll().forEach(chargePoint -> {
            FakeChargePoint fakeChargePoint = new FakeChargePoint(chargePoint.getOcppId(), mockChargingSessionServices, mockConnectorServices);
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
            // TODO burada Java nın completabple future ile
            // TODO mock_connector tablosundaki status alanını Pereparing'e çekip central-system'a sendStatusNotificationRequest atalım.
            // TODO daha sonra sendStartTransactionRequest atalım
            // TODO mock_connector tablosundaki status alanını Charging'e çekip central-system'a sendStatusNotificationRequest atalım.
            // TODO bu 3 işlemi arka arkaya birbirini takip eden 3 admımmış gibi yalaım. 1. adım tamamlanınca 2 ye geçelim, 2 tamamlanınca 3 e geçelim.
            mockChargingSessionServices.activateNewChargingSession(session.getId());
            getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint -> fakeChargePoint.sendStartTransactionRequest(session.getConnectorId(), session.getIdTag(), session.getMeterStart()));
        }
    }

    @Scheduled(fixedRate = 10000)
    public void stopTransactionScheduler() {
        List<MockChargingSession> sessions = mockChargingSessionServices.findByStatus(SessionStatus.FINISHING);
        for (MockChargingSession session : sessions) {
            // TODO burada Java nın completabple future ile
            // TODO mock_connector tablosundaki status alanını Finishing'e çekip central-system'a sendStatusNotificationRequest atalım.
            // TODO daha sonra sendStopTransactionRequest atalım
            // TODO mock_connector tablosundaki status alanını Available'e çekip central-system'a sendStatusNotificationRequest atalım.
            // TODO bu 3 işlemi arka arkaya birbirini takip eden 3 admımmış gibi yalaım. 1. adım tamamlanınca 2 ye geçelim, 2 tamamlanınca 3 e geçelim.
            mockChargingSessionServices.sendStopTransactionRequest(session.getId());
            getFakeChargePoint(session.getChargePointOcppId()).ifPresent(fakeChargePoint -> fakeChargePoint.sendStopTransactionRequest(session.getMeterStop(), session.getId()));
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

    /**
     * TODO  online olmayanları al  ve bir listede tut 5 dakikada bir  baglanmaya çalışsın
     */
    @Scheduled(fixedRate = 50000)
    public void reconnetScheduler() {
        chargePointsLostList = mockChargePointServices.findByOnline(false);
        chargePointsLostList.forEach(chargePoint -> {
            log.info("reconnecting to " + chargePoint.getOcppId());
            startServer();
        });

    }
}
