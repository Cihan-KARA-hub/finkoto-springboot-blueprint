package com.finkoto.chargestation.api.controller;


import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.services.ChargePointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/charge-points")
public class ChargePointController {
    private final ChargePointService chargePointService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<ChargePointDto>> getAllChargePoints(
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity.ok(chargePointService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargePointDto> chargeGetById(@PathVariable Long id) {
        return ResponseEntity.ok(chargePointService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ChargePointDto> createChargePoint(@RequestBody ChargePointDto chargePointDto) {
        chargePointService.create(chargePointDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChargePoint(@PathVariable Long id) {
        chargePointService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> updateChargePoint(@RequestBody ChargePointDto chargePointDto) {
        chargePointService.update(chargePointDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
