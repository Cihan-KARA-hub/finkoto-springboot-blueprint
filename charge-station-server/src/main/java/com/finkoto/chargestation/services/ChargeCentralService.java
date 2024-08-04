package com.finkoto.chargestation.services;


import com.finkoto.chargestation.model.ChargeCentralModel;
import com.finkoto.chargestation.repository.ChargeCentralRepository;
import com.finkoto.chargestation.repository.ChargePointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ChargeCentralService {
    @Autowired
    private ChargeCentralRepository chargeCentralRepository;
    private ChargePointRepository chargePointRepository;

    public List<ChargeCentralModel> getAll(){
        return chargeCentralRepository.findAll();
    }
    public  void  delete(@PathVariable int id){
        chargeCentralRepository.deleteById(id);
    }
    public void   create(ChargeCentralModel chargeCentralModel){
        chargeCentralRepository.save(chargeCentralModel);
    }

    public Optional<ChargeCentralModel> findById(int id) {
        return chargeCentralRepository.findById(id);
    }
    public ChargeCentralModel update(int id, ChargeCentralModel updatedChargeCentral) {
        Optional<ChargeCentralModel> existingChargeCentral = chargeCentralRepository.findById(id);

        if (existingChargeCentral.isPresent()) {
            ChargeCentralModel chargeCentral = existingChargeCentral.get();
            chargeCentral.setChargeCentralId(updatedChargeCentral.getChargeCentralId()); // Örnek olarak 'name' alanını güncelliyoruz

            return chargeCentralRepository.save(chargeCentral);
        } else {
            throw new EntityNotFoundException("ChargeCentral with id " + id + " not found.");
        }
    }

}
