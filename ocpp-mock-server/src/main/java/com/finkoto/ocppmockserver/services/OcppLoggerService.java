package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.OcppLogger;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.repository.MockOcppLoggerRepository;
import com.finkoto.ocppmockserver.services.specification.OcppLoggerSpecification;
import eu.chargetime.ocpp.model.core.RemoteStartTransactionRequest;
import eu.chargetime.ocpp.model.core.RemoteStopTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class OcppLoggerService {
    private final MockOcppLoggerRepository ocppLoggerRepository;
    private final MockConnectorServices connectorService;
    private final MockChargingSessionServices mockChargingSessionServices;


    @Transactional
    public List<OcppLogger> filterLogs(String chargePointOcppId, String connectorOcppId, String chargingSessionId) {
        Specification<OcppLogger> spec = Specification.where(OcppLoggerSpecification.hasChargePointOcppId(chargePointOcppId))
                .and(OcppLoggerSpecification.hasConnectorOcppId(connectorOcppId))
                .and(OcppLoggerSpecification.hasChargingSessionId(chargingSessionId));
        return ocppLoggerRepository.findAll((Sort) spec);
    }

    @Transactional
    public void handleRemoteStartTransactionRequestLogger(RemoteStartTransactionRequest request) {
        OcppLogger logger = new OcppLogger();
        String cpId = connectorService.getChargePointId(request.getConnectorId());
        List<MockChargingSession> sessionId = mockChargingSessionServices.findByConnectorId(request.getConnectorId());
        for (MockChargingSession session : sessionId) {
            if (session.getStatus() == SessionStatus.NEW) {
                logger.setConnectorOcppId(request.getConnectorId().toString());
                logger.setChargingSessionId(session.getId().toString());
                logger.setChargePointOcppId(cpId);
                logger.setInfo("Start new charge sessions (handle remote start transaction) {}" + request.getConnectorId());
                ocppLoggerRepository.save(logger);
            }
        }

    }

    @Transactional
    public void sendStartTransactionRequestLogger(int connectorId, String idTag) {
        OcppLogger logger = new OcppLogger();
        String cpId = connectorService.getChargePointId(connectorId);
        logger.setChargePointOcppId(cpId);
        logger.setConnectorOcppId(String.valueOf(connectorId));
        logger.setChargingSessionId(idTag);
        logger.setInfo("Start new charge sessions (send start transaction) {}" + idTag);
    }

    @Transactional
    public void meterValueLog(int connectorId, String idTag, String meterValue) {
        OcppLogger logger = new OcppLogger();
        String cpId = connectorService.getChargePointId(connectorId);
        logger.setChargePointOcppId(cpId);
        logger.setConnectorOcppId(String.valueOf(connectorId));
        logger.setChargingSessionId(idTag);
        logger.setInfo("Start new meter values (meter) {}" + meterValue);
        ocppLoggerRepository.save(logger);
    }

    @Transactional
    public void handleRemoteStopTransactionRequestLogger(RemoteStopTransactionRequest request) {
        OcppLogger logger = new OcppLogger();
        Optional<MockChargingSession> sessionId = mockChargingSessionServices.findById(Long.valueOf(request.getTransactionId()));
        logger.setChargingSessionId(sessionId.get().getId().toString());
        logger.setConnectorOcppId(String.valueOf(sessionId.get().getConnectorId()));
        logger.setChargePointOcppId(sessionId.get().getChargePointOcppId());
        logger.setInfo("handle remote stop transaction {}" + request.getTransactionId().toString());
    }

    @Transactional
    public void sendStopTransactionRequestLogger(long id) {
        OcppLogger logger = new OcppLogger();
        Optional<MockChargingSession> cpId = mockChargingSessionServices.findById(id);
        logger.setChargingSessionId(cpId.get().getId().toString());
        logger.setConnectorOcppId(String.valueOf(cpId.get().getConnectorId()));
        logger.setChargePointOcppId(cpId.get().getChargePointOcppId());
        logger.setInfo("Send remote stop transaction {}" + id);

    }


}
