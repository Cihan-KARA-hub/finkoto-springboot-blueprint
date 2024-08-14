package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.Connector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
    Page<Connector> findAllByChargePointId(Pageable pageable, Long chargePointId);
    Long findIdByChargePointId(Long chargePointId);
}
