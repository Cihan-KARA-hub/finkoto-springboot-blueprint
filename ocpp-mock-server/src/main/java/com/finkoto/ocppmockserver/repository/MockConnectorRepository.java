package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.Connector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MockConnectorRepository extends JpaRepository<Connector, Long> {
    Long findIdByChargePointId(Long chargePointId);
    Optional<Connector> findByOcppIdAndChargePoint_OcppId(int connectorOcppId,String chargePointOcppId);

    Connector findStatusByChargePointId(Long chargePointId);

    Page<Connector> findAllByChargePointId(Pageable pageable, Long chargePointId);

}
