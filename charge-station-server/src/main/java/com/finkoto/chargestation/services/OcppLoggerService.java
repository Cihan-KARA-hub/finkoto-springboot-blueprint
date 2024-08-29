package com.finkoto.chargestation.services;

import com.finkoto.chargestation.api.mapper.OcppLoggerMapper;
import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.OcppLogger;
import com.finkoto.chargestation.repository.ConnectorRepository;
import com.finkoto.chargestation.repository.OcppLoggerRepository;
import com.finkoto.chargestation.services.specification.OcppLoggerSpecification;
import eu.chargetime.ocpp.model.core.MeterValuesRequest;
import eu.chargetime.ocpp.model.core.StartTransactionRequest;
import eu.chargetime.ocpp.model.core.StopTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OcppLoggerService {
    private final OcppLoggerRepository ocppLoggerRepository;
    private final OcppLoggerMapper ocppLoggerMapper;
    private final ConnectorRepository connectorRepository;
    private final ConnectorService connectorService;
    private final ChargingSessionService chargingSessionService;


    @Transactional
    public List<OcppLogger> filterLogs(String chargePointOcppId, String connectorOcppId, String chargingSessionId) {
        Specification<OcppLogger> spec = Specification.where(OcppLoggerSpecification.hasChargePointOcppId(chargePointOcppId))
                .and(OcppLoggerSpecification.hasConnectorOcppId(connectorOcppId))
                .and(OcppLoggerSpecification.hasChargingSessionId(chargingSessionId));
        return ocppLoggerRepository.findAll((Sort) spec);
    }

    @Transactional
    public void startSave(StartTransactionRequest request) {
        OcppLogger logger = new OcppLogger();
        Integer connectorId = request.getConnectorId();
        String byChargePointId = connectorService.getChargePointId(connectorId);
        logger.setChargePointOcppId(byChargePointId);
        logger.setConnectorOcppId(request.getConnectorId().toString());
        logger.setChargingSessionId(request.getIdTag());
        logger.setInfo("Start " + logger.getChargePointOcppId());
        ocppLoggerRepository.save(logger);
    }

    @Transactional
    public void stopSave(StopTransactionRequest request) {
        OcppLogger logger = new OcppLogger();
        Optional<ChargingSession> optional = chargingSessionService.findById(request.getTransactionId());
        logger.setChargePointOcppId(optional.get().getChargePointOcppId());
        logger.setConnectorOcppId(String.valueOf(optional.get().getConnectorId()));
        logger.setChargingSessionId(optional.get().getId().toString());
        logger.setInfo("Stop " + logger.getChargePointOcppId());
        ocppLoggerRepository.save(logger);
    }

    @Transactional
    public void delete(long id) {
        ocppLoggerRepository.deleteById(id);
    }

    public void meterValueLog(MeterValuesRequest request) {
        OcppLogger logger = new OcppLogger();
        Optional<ChargingSession> optional = chargingSessionService.findById(request.getTransactionId());
        logger.setChargePointOcppId(optional.get().getChargePointOcppId());
        logger.setConnectorOcppId(request.getConnectorId().toString());
        logger.setChargingSessionId(request.getTransactionId().toString());
        logger.setInfo("Hear Beat Meter Value " + Arrays.toString(request.getMeterValue()) + "-> " + logger.getConnectorOcppId());
        ocppLoggerRepository.save(logger);
    }
}
