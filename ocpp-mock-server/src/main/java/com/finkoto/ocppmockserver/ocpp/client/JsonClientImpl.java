package com.finkoto.ocppmockserver.ocpp.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finkoto.ocppmockserver.controller.ChargeHardwareSpecController;
import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
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
    private final Map<String, FakeChargePoint> connections = new HashMap<>();

    private final ChargePointRepository chargePointRepository;
    ChargeHardwareSpec chargeHardwareSpec;
    ChargeHardwareSpecController chargeHardwareSpecController;

    @Value("${websocket.url}")
    private String webSocketUrl;
    final FakeChargePoint fakeChargePoint = new FakeChargePoint();

    @PostConstruct
    public void startServer() throws Exception {
        chargePointRepository.findAll().forEach(chargePoint -> {
            fakeChargePoint.connect(chargePoint.getOcppId(), webSocketUrl);
            connections.put(chargePoint.getOcppId(), fakeChargePoint);
            // TODO cihaz bilgilerini apiden girip, tablodan okuyup gondermek lazim
            //api kısmını  charge station server kısmında  yazılacak
            //burdan çagıracam

            fakeChargePoint.sendBootNotification(chargeHardwareSpec.getChargePointVendor(), chargeHardwareSpec.getChargePointModel() );
        });
    }
    @PostConstruct
    @Scheduled(fixedRate = 10000)
    public void interrupt(){
        List<ChargePoint> onlineChargePoints = chargePointRepository.findByOnline(true);
        onlineChargePoints.forEach(chargePoint -> {
          fakeChargePoint.sendHeartbeatRequest();
        });
    }

    // TODO, online=true olan chargePointleri sorgula, heartbeat yolla +
}
