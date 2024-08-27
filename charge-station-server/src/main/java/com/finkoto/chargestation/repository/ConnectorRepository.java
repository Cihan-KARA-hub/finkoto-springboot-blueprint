package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.Connector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
    Page<Connector> findAllByChargePointId(Pageable pageable, Long chargePointId);
    Optional<Connector> findByIdAndOcppId(Long id, int ocppId);


}
