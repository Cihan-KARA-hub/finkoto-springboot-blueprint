package com.finkoto.chargestation.services;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ChargingSessionMapper;
import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.enums.SessionStatus;
import com.finkoto.chargestation.ocpp.OCPPCentralSystem;
import com.finkoto.chargestation.repository.ChargingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChargingSessionService {
    private final OCPPCentralSystem centralSystem;
    private final ChargingSessionRepository chargingSessionRepository;
    private final ChargingSessionMapper chargingSessionMapper;

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
    public List<ChargingSession> findByStatus(SessionStatus status) {
        return chargingSessionRepository.findByStatus(status);
    }


    @Transactional
    public ChargingSession activateNewChargingSession(Long id, int connectorId) {
        ChargingSession chargingSession = chargingSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        chargingSession.setStatus(SessionStatus.ACTIVE);
        chargingSession.setConnectorId(connectorId);
        chargingSession.setBeginTime(OffsetDateTime.now());
        return chargingSessionRepository.save(chargingSession);
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
    public void remoteStop( String ocppId, int connectorId) {
        ChargingSession session = chargingSessionRepository.findByChargePointOcppId(ocppId);
        if (session.getConnectorId() == connectorId && session.getStatus() == SessionStatus.ACTIVE && session.getChargePointOcppId().equals(ocppId)) {
            session.setStatus(SessionStatus.FINISHED);
            session.setEndTime(OffsetDateTime.now());
            int value = Integer.parseInt(session.getCurrMeter());
            session.setMeterStop(value);
            centralSystem.sendRemoteStopTransactionRequest(session.getChargePointOcppId(), session.getIdTag());
        }
    }
}
