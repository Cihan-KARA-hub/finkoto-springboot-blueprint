package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.OcppLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockOcppLoggerRepository extends JpaRepository<OcppLogger, Long> {
}
