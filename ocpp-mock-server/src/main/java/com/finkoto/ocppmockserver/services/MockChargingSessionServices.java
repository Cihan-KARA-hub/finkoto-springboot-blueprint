package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.repository.MockChargingSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MockChargingSessionServices {

    private final MockChargingSessionRepository mockChargingSessionRepository;

    public List<MockChargingSession> findByStatus(SessionStatus status) {
        return mockChargingSessionRepository.findByStatus(status);
    }

    @Transactional
    public void handleRemoteStartTransactionRequest(String chargePointOcppId, int connectorId, String idTag) {
        MockChargingSession mockChargingSession = new MockChargingSession();
        mockChargingSession.setStatus(SessionStatus.NEW);
        mockChargingSession.setConnectorId(connectorId);
        mockChargingSession.setIdTag(idTag);
        mockChargingSession.setChargePointOcppId(chargePointOcppId);
        mockChargingSessionRepository.save(mockChargingSession);
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
    public void remoteStopTransactionRequest(int idTag ) {
        String id = String.valueOf(idTag);
        MockChargingSession mockChargingSession = mockChargingSessionRepository.findByIdTag(id).orElseThrow(() -> new EntityNotFoundException("Entity with id: " + id + " not found."));
        if (mockChargingSession.getStatus() == SessionStatus.ACTIVE) {
            mockChargingSession.setStatus(SessionStatus.FINISHED);
            mockChargingSession.setEndTime(OffsetDateTime.now());
            mockChargingSession.setMeterStop(Integer.valueOf(mockChargingSession.getCurrMeter()));
            mockChargingSessionRepository.save(mockChargingSession);
        } else {
            System.out.println("remote stop transaction not exist");
        }

    }

}
