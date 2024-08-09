package com.finkoto.ocppmockserver.ocpp.client;

import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.repository.ChargePointRepository;
import com.finkoto.ocppmockserver.server.FakeChargePoint;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonClientImpl {
    final FakeChargePoint fakeChargePoint = new FakeChargePoint();
    private final Map<String, FakeChargePoint> connections = new HashMap<>();
    private final ChargePointRepository chargePointRepository;
    @Value("${websocket.url}")
    private String webSocketUrl;

    @PostConstruct
    public void startServer() throws Exception {
        chargePointRepository.findAll().forEach(chargePoint -> {
            fakeChargePoint.connect(chargePoint.getOcppId(), webSocketUrl);
            connections.put(chargePoint.getOcppId(), fakeChargePoint);
            fakeChargePoint.sendBootNotification(chargePoint.getChargeHardwareSpec());
        });
    }

    @Scheduled(fixedRate = 10000)
    public void interrupt() {
        List<ChargePoint> onlineChargePoints = chargePointRepository.findByOnline(true);
        onlineChargePoints.forEach(chargePoint -> {
            fakeChargePoint.sendHeartbeatRequest();
        });
    }

    // TODO mock_charging_session tablosunda aktif şarj varsa meterValue istei yolla.
    // random int 0-100 üret currMeter = currMeter + random
}
