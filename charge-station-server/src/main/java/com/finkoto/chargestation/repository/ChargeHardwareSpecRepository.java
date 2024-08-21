package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargeHardwareSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeHardwareSpecRepository extends JpaRepository<ChargeHardwareSpec, Long> {
}
