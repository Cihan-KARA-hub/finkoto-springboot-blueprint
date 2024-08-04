package com.finkoto.chargestation.controller;


import com.finkoto.chargestation.model.ChargeCentralModel;
import com.finkoto.chargestation.services.ChargeCentralService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/charge-central")

public class ChargeCentralController {

    @Autowired
    private ChargeCentralService chargeCentralService;

    @GetMapping("/all-central")
    public List<ChargeCentralModel> allCentral() {
        List<ChargeCentralModel> centrals = chargeCentralService.getAll();
        return centrals; // 200 OK ile birlikte listeyi döner
    }

    @DeleteMapping("/delete-central/{id}")
    public ResponseEntity<Void> deleteCentral(@PathVariable int id) {
        chargeCentralService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content döner
    }

    @PostMapping("/create-central")
    public ResponseEntity<Void> create(@RequestBody ChargeCentralModel model) {
        chargeCentralService.create(model);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created döner
    }
    @GetMapping("/id-central/{id}")
    public ResponseEntity<ChargeCentralModel> findById(@PathVariable int id) {
        Optional<ChargeCentralModel> central = chargeCentralService.findById(id);
        return central.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/update-central/{id}")
    public ResponseEntity<ChargeCentralModel> updateCentral(@PathVariable int id, @RequestBody ChargeCentralModel updatedChargeCentral) {
        try {
            ChargeCentralModel updated = chargeCentralService.update(id, updatedChargeCentral);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
