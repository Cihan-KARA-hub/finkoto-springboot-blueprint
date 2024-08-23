package com.finkoto.chargestation.services;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ChargingSessionMapper;
import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.enums.SessionStatus;
import com.finkoto.chargestation.ocpp.OCPPCentralSystem;
import com.finkoto.chargestation.repository.ChargingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChargingSessionService {
    private static final Logger log = LoggerFactory.getLogger(ChargingSessionService.class);
    private final OCPPCentralSystem centralSystem;
    private final ChargingSessionRepository chargingSessionRepository;
    private final ChargingSessionMapper chargingSessionMapper;
    private final ConnectorService connectorService;

    @Transactional
    public PageableResponseDto<ChargingSessionDto> getAll(Pageable pageable) {
        final Page<ChargingSession> pageData = chargingSessionRepository.findAll(pageable);
        final List<ChargingSessionDto> chargePointDtoList = chargingSessionMapper.toDtoList(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public ChargingSessionDto findById(Long id) {
        ChargingSession chargingSession = chargingSessionRepository.findById(id).orElse(null);
        return chargingSessionMapper.toDto(chargingSession);
    }

    @Transactional
    public Optional<ChargingSession> findById(long id) {
        return chargingSessionRepository.findById(id);
    }

    @Transactional
    public List<ChargingSession> findByStatus(SessionStatus status) {
        return chargingSessionRepository.findByStatus(status);
    }

    @Transactional
    public Optional<ChargingSession> findNewChargingSession(String ocppId, int connectorOcppId, String idTag, SessionStatus status) {
        return chargingSessionRepository.findByChargePointOcppIdAndConnectorIdAndIdTagAndStatus(ocppId, connectorOcppId, idTag, status);
    }

    @Transactional
    public Optional<ChargingSession> findNewStopChargingSession(String ocppId, int connectorOcppId, String idTag, SessionStatus status) {
        return chargingSessionRepository.findByChargePointOcppIdAndConnectorIdAndIdTagAndStatus(ocppId, connectorOcppId, idTag, status);
    }

    @Transactional
    public void newChargingSession(String ocppId, int connectorId, String idTag) {
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(SessionStatus.NEW);
        chargingSession.setConnectorId(connectorId);
        chargingSession.setIdTag(idTag);
        chargingSession.setChargePointOcppId(ocppId);
        chargingSessionRepository.save(chargingSession);
    }

    @Transactional
    public void activateNewChargingSession(Long id, Integer meterStart) {
        final ChargingSession chargingSession = chargingSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        chargingSession.setBeginTime(OffsetDateTime.now());
        chargingSession.setStatus(SessionStatus.ACTIVE);
        chargingSession.setMeterStart(meterStart);
        chargingSession.setCurrMeter(String.valueOf(meterStart));
        chargingSessionRepository.save(chargingSession);
    }

    @Transactional
    public void handleMeterValuesRequest(String meterValue) {
        chargingSessionRepository.findByStatus(SessionStatus.ACTIVE).forEach(chargingSession -> {
            if (chargingSession.getCurrMeter() != null) {
                int meterValues = Integer.parseInt(chargingSession.getCurrMeter()) + Integer.parseInt(meterValue);
                chargingSession.setCurrMeter(String.valueOf(meterValues));
                chargingSessionRepository.save(chargingSession);
            } else {
                chargingSession.setCurrMeter("0");
                chargingSessionRepository.save(chargingSession);
            }
        });
    }

    @Transactional
    public void sendRemoteStopTransactionRequest(String ocppId, int connectorId, String idTag) {
        final ChargingSession chargingSession = chargingSessionRepository.findByChargePointOcppIdAndConnectorIdAndIdTag(ocppId, connectorId, idTag).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + idTag + " not found."));
        centralSystem.sendRemoteStopTransactionRequest(chargingSession.getChargePointOcppId(), chargingSession.getIdTag());
    }

    @Transactional
    public void handleStopTransactionRequest(long id) {
        final Optional<ChargingSession> optional = chargingSessionRepository.findById(id);
        if (optional.isEmpty()) {
            return;
        }
        final ChargingSession chargingSession = optional.get();
        chargingSession.setStatus(SessionStatus.FINISHED);
        chargingSessionRepository.save(chargingSession);
    }

    public boolean checkActiveSession(String ocppId, int connectorId, SessionStatus status) {
        Optional<ChargingSession> session = chargingSessionRepository.findByChargePointOcppIdAndConnectorIdAndStatus(ocppId, connectorId, status);
        return session.isEmpty();
    }
}
