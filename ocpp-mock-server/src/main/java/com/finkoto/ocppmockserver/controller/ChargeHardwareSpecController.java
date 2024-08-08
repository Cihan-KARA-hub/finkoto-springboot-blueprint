package com.finkoto.ocppmockserver.controller;


import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
import com.finkoto.ocppmockserver.repository.ChargeHardwareSpecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/charge-hardware-spec")
public class ChargeHardwareSpecController {
    private final ChargeHardwareSpecRepository chargeHardwareSpecRepository;

    @GetMapping
    public List<ChargeHardwareSpec> getChargeHardwareSpec() throws  Exception {
       return chargeHardwareSpecRepository.findAll();
    }
    @PostMapping
    public ResponseEntity<ChargeHardwareSpec> createChargeHardwareSpec(@RequestBody ChargeHardwareSpec chargeHardwareSpec) {
        try {
            ChargeHardwareSpec savedChargeHardwareSpec = chargeHardwareSpecRepository.save(chargeHardwareSpec);
            return new ResponseEntity<>(savedChargeHardwareSpec, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
