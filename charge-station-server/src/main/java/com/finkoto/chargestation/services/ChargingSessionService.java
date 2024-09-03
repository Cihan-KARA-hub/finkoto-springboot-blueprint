package com.finkoto.chargestation.services;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ChargingSessionMapper;
import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.Connector;
import com.finkoto.chargestation.model.enums.ConnectorStatus;
import com.finkoto.chargestation.model.enums.SessionStatus;
import com.finkoto.chargestation.ocpp.OCPPCentralSystem;
import com.finkoto.chargestation.repository.ChargingSessionRepository;
import com.finkoto.chargestation.repository.ConnectorRepository;
import eu.chargetime.ocpp.model.core.AuthorizationStatus;
import eu.chargetime.ocpp.model.core.IdTagInfo;
import eu.chargetime.ocpp.model.core.StartTransactionConfirmation;
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
    private final ConnectorRepository connectorRepository;

    @Transactional
    public PageableResponseDto<ChargingSessionDto> getAll(Pageable pageable) {
        final Page<ChargingSession> pageData = chargingSessionRepository.findAll(pageable);
        final List<ChargingSessionDto> chargePointDtoList = chargingSessionMapper.toDtoList(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public ChargingSessionDto findByIdDto(Long id) {
        ChargingSession chargingSession = chargingSessionRepository.findById(id).orElse(null);
        return chargingSessionMapper.toDto(chargingSession);
    }

    @Transactional
    public Optional<ChargingSession> findById(long id) {
        return Optional.ofNullable(chargingSessionRepository.findById(id).orElse(null));
    }

    @Transactional
    public StartTransactionConfirmation findNewChargingSession(int connectorId, String idTag) {
        Optional<ChargingSession> sessionOptional = chargingSessionRepository.findById(Long.valueOf(idTag));
        if (sessionOptional.isPresent() && sessionOptional.get().getStatus() == SessionStatus.NEW) {
            activateNewChargingSession(sessionOptional.get().getId(), idTag);
            final IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted);
            return new StartTransactionConfirmation(idTagInfo, connectorId);
        } else {
            log.info("No charging session found");
            return new StartTransactionConfirmation(new IdTagInfo(AuthorizationStatus.Invalid), 0);
        }


    }

    @Transactional
    public ChargingSession newChargingSession(Long connectorId, int ocppId) {
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(SessionStatus.NEW);
        chargingSession.setConnectorId(Math.toIntExact(connectorId));
        chargingSession.setChargePointOcppId(String.valueOf(ocppId));
        chargingSessionRepository.save(chargingSession);
        return chargingSession;
    }

    @Transactional
    public void activateNewChargingSession(Long id, String idTag) {
        final ChargingSession chargingSession = chargingSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        chargingSession.setBeginTime(OffsetDateTime.now());
        chargingSession.setStatus(SessionStatus.ACTIVE);
        chargingSession.setIdTag(idTag);
        chargingSession.setMeterStart(0);
        chargingSession.setCurrMeter("0");
        chargingSessionRepository.save(chargingSession);
    }

    @Transactional
    public void handleMeterValuesRequest(int id, String meterValue) {
        ChargingSession chargingSession = chargingSessionRepository.findById((long) id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        chargingSession.addMeterValue(meterValue);
        chargingSessionRepository.save(chargingSession);
    }

    @Transactional
    public boolean sendRemoteStopTransactionRequest(Long id) {
        final ChargingSession chargingSession = chargingSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        if (chargingSession != null && chargingSession.getStatus() == SessionStatus.ACTIVE) {
            centralSystem.sendRemoteStopTransactionRequest(chargingSession.getChargePointOcppId(), chargingSession.getId());
            return true;
        } else {
            return false;
        }

    }

    @Transactional
    public void handleStopTransactionRequest(long id) {
        final Optional<ChargingSession> optional = chargingSessionRepository.findById(id);
        if (optional.isEmpty()) {
            return;
        }
        final ChargingSession chargingSession = optional.get();
        chargingSession.setMeterStop(Integer.valueOf(chargingSession.getCurrMeter()));
        chargingSession.setEndTime(OffsetDateTime.now());
        chargingSession.setStatus(SessionStatus.FINISHED);
        chargingSessionRepository.save(chargingSession);
    }

    @Transactional
    public Exception sendRemoteStartTransactionRequest(Long connectorId, int ocppId) {
        Optional<Connector> connector = connectorRepository.findByIdAndOcppId(connectorId, ocppId);
        if (connector.isPresent() && connector.get().getStatus() == ConnectorStatus.Available) {
            centralSystem.sendRemoteStartTransactionRequest(connectorId, ocppId);
        } else {
            throw new EntityNotFoundException("Entity with id: " + connectorId + " not found.");
        }
        return null;
    }
}
