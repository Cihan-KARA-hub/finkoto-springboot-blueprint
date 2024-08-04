package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargePointModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChargePointRepository extends JpaRepository<ChargePointModel, Integer> {
}
