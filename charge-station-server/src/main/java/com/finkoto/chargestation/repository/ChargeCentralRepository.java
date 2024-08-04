package com.finkoto.chargestation.repository;

import com.finkoto.chargestation.model.ChargeCentralModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChargeCentralRepository extends JpaRepository<ChargeCentralModel,Integer >{
}
