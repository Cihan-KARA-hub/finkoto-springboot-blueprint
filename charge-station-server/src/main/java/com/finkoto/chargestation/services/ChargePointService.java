package com.finkoto.chargestation.services;


import com.finkoto.chargestation.model.ChargePointModel;
import com.finkoto.chargestation.repository.ChargePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargePointService {
    @Autowired
    private ChargePointRepository chargePointRepository;

    public List<ChargePointModel> getAll(){
        return chargePointRepository.findAll();
    }
    public  void  delete(int id ){
        chargePointRepository.deleteById(id);
    }
    public ChargePointModel create(ChargePointModel chargePointModel){
        chargePointRepository.save(chargePointModel);
        return chargePointModel;
    }
    public Optional<ChargePointModel> findById(int id){
        chargePointRepository.findById(id);
       return chargePointRepository.findById(id);
    }

}
