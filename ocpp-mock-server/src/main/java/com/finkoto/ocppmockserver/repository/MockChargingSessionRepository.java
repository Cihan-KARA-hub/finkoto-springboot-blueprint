package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MockChargingSessionRepository extends JpaRepository<MockChargingSession, Long> {
    List<MockChargingSession> findByStatus(SessionStatus status);
    Optional<MockChargingSession> findByIdTag(String idTag);

    Optional<MockChargingSession> findByChargePointOcppIdAndConnectorIdAndStatus(String chargePointOcppId, int connectorId, SessionStatus status);

    Long findByConnectorId(Integer connectorId);
}

