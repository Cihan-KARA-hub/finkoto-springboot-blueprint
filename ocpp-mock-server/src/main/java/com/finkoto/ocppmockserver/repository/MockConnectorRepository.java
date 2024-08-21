package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.Connector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockConnectorRepository extends JpaRepository<Connector, Long> {
    Long findIdByChargePointId(Long chargePointId);
    Connector findStatusByChargePointId(Long chargePointId);
    Page<Connector> findAllByChargePointId(Pageable pageable, Long chargePointId);
}
