package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.Connector;
import com.finkoto.ocppmockserver.model.enums.ConnectorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
    Long findIdByChargePointId(Long chargePointId);
    Connector findStatusByChargePointId(Long chargePointId);
}
