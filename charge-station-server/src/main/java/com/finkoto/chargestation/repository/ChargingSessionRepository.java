package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargingSession;
import com.finkoto.chargestation.model.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {

    List<ChargingSession> findByConnectorId(int connectorId );
    Optional<ChargingSession> findByIdTag(String tag);

}
