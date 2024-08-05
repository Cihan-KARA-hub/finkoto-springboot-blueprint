package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {
}
