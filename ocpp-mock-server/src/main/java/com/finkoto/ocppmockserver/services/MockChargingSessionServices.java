package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.repository.MockChargingSessionRepository;
import eu.chargetime.ocpp.model.core.RemoteStartStopStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MockChargingSessionServices {

    private final MockChargingSessionRepository mockChargingSessionRepository;
    private final MockConnectorServices mockConnectorServices;

    public List<MockChargingSession> findByStatus(SessionStatus status) {
        return mockChargingSessionRepository.findByStatus(status);
    }

    @Transactional
    public RemoteStartStopStatus handleRemoteStartTransactionRequest(String chargePointOcppId, int connectorId, String idTag) {
        Optional<MockChargingSession> session = mockChargingSessionRepository.findByChargePointOcppIdAndConnectorIdAndStatus(chargePointOcppId, connectorId, SessionStatus.ACTIVE);
        if (session.isPresent()) {
            return RemoteStartStopStatus.Rejected;
        } else {
            MockChargingSession mockChargingSession = new MockChargingSession();
            mockChargingSession.setStatus(SessionStatus.NEW);
            mockChargingSession.setConnectorId(connectorId);
            mockChargingSession.setIdTag(idTag);
            mockChargingSession.setChargePointOcppId(chargePointOcppId);
            mockChargingSessionRepository.save(mockChargingSession);
            return RemoteStartStopStatus.Accepted;
        }
    }

    @Transactional
    public void activateNewChargingSession(Long id) {
        MockChargingSession mockChargingSession = mockChargingSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        mockChargingSession.setStatus(SessionStatus.ACTIVE);
        mockChargingSessionRepository.save(mockChargingSession);
    }

    @Transactional
    public String updateMeterValue(Long id) {
        MockChargingSession mockChargingSession = mockChargingSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        int random = (int) (Math.random() * 100);
        if (mockChargingSession.getCurrMeter() == null) {
            mockChargingSession.setCurrMeter("0");
        }
        random = random + Integer.parseInt(mockChargingSession.getCurrMeter());
        mockChargingSession.setCurrMeter(String.valueOf(random));
        return String.valueOf(random);
    }

    @Transactional
    public RemoteStartStopStatus remoteStopTransactionRequest(int idTag) {
        final Optional<MockChargingSession> optional = mockChargingSessionRepository.findByIdTag(String.valueOf(idTag));
        if (optional.isEmpty()) {
            return RemoteStartStopStatus.Rejected;
        }
        final MockChargingSession mockChargingSession = optional.get();
        mockChargingSession.setStatus(SessionStatus.FINISHING);
        mockChargingSession.setMeterStop(Integer.valueOf(mockChargingSession.getCurrMeter()));
        mockChargingSessionRepository.save(mockChargingSession);
        return RemoteStartStopStatus.Accepted;
    }

    @Transactional
    public void sendStopTransactionRequest(Long id) {
        // TODO .orElseThrove ekleyelim.
        final Optional<MockChargingSession> optional = mockChargingSessionRepository.findById(id);
        final MockChargingSession mockChargingSession = optional.get();
        mockChargingSession.setStatus(SessionStatus.FINISHED);
        mockChargingSession.setEndTime(OffsetDateTime.now());
        mockChargingSession.setMeterStop(Integer.valueOf(mockChargingSession.getCurrMeter()));
        mockChargingSessionRepository.save(mockChargingSession);

    }

    public String findByCurrMeterValue(long id) {
        final Optional<MockChargingSession> optional = mockChargingSessionRepository.findById(id);
        optional.orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        return optional.get().getCurrMeter();
    }
}
