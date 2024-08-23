package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {
    List<ChargingSession> findByStatus(SessionStatus status);

    Optional<ChargingSession> findByChargePointOcppIdAndConnectorIdAndIdTagAndStatus(String ocppId, int connector, String idTag, SessionStatus status);

    Optional<ChargingSession> findByChargePointOcppIdAndConnectorIdAndIdTag(String ocppId, int connector, String idTag);
    Optional<ChargingSession> findByChargePointOcppIdAndConnectorIdAndStatus(String ocppId, int connector, SessionStatus status);

    Optional<ChargingSession> findByIdTag(String idTag);
}
