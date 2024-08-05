package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChargePointRepository extends JpaRepository<ChargePoint, Long> {
}
