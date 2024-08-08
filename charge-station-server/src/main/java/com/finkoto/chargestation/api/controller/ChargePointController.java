package com.finkoto.chargestation.api.controller;


import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.services.ChargePointService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/charge-points")
public class ChargePointController {
    private final ChargePointService chargePointService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<ChargePointDto>> getAllChargePoints(
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(chargePointService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargePointDto> chargeGetById(@PathVariable Long id) {
        return Optional.ofNullable(chargePointService.findById(id)).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createChargePoint(@RequestBody ChargePointDto chargePointDto) {
        chargePointService.create(chargePointDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargePointDto> updateChargePoint(@PathVariable Long id, @RequestBody ChargePointDto chargePointDto) {
        return ResponseEntity.ok(chargePointService.update(id, chargePointDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChargePoint(@PathVariable Long id) {
        chargePointService.delete(id);
        return ResponseEntity.noContent().build();
    }

}