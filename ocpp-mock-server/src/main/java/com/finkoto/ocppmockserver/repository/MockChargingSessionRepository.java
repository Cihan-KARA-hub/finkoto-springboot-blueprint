package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MockChargingSessionRepository extends JpaRepository<MockChargingSession, Long> {
    List<MockChargingSession> findByStatus(SessionStatus status);
}
