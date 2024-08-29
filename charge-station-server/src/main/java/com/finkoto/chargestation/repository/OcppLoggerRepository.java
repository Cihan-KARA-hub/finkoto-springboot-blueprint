package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.OcppLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcppLoggerRepository extends JpaRepository<OcppLogger, Long> {
}
