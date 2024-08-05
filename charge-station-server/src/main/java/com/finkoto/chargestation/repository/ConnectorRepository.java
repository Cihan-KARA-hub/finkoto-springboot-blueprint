package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
}
