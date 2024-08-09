package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChargePointRepository extends JpaRepository<ChargePoint, Long> {
    Optional<ChargePoint> findByOcppId(String ocppId);
    List<ChargePoint> findByOnline (boolean online);
}
