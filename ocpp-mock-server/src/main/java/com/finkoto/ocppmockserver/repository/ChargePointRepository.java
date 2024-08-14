package com.finkoto.ocppmockserver.repository;


import com.finkoto.ocppmockserver.model.ChargePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargePointRepository extends JpaRepository<ChargePoint, Long> {
   List<ChargePoint>  findByOnline (boolean online);

}
