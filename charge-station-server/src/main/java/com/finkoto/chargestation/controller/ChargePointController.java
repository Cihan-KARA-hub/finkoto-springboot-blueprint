package com.finkoto.chargestation.controller;


import com.finkoto.chargestation.model.ChargePointModel;
import com.finkoto.chargestation.services.ChargePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chargeg-point")
public class ChargePointController {
    @Autowired
    ChargePointService chargePointService;
    @GetMapping("/all-point")
    public ResponseEntity<List<ChargePointModel>> allCentral() {
        List<ChargePointModel> centrals = chargePointService.getAll();
        return ResponseEntity.ok(centrals);
    }
    @GetMapping("/charge-point/{id}")
    public Optional<ChargePointModel> chargePoint(@PathVariable int id) {
        Optional<ChargePointModel> centrals = chargePointService.findById(id);
        return centrals;
    }
    @PostMapping("/create-central-point")
    public ResponseEntity<ChargePointModel> createCentral(@RequestBody ChargePointModel central) {
        chargePointService.create(central);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created döner
    }
    @DeleteMapping("/charge-point-delete/{id}")
    public ResponseEntity<Void> deleteCentral(@PathVariable int id) {
        chargePointService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
