package com.finkoto.ocppmockserver.repository;

import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeHardwareSpecRepository extends JpaRepository<ChargeHardwareSpec,Long> {

}
