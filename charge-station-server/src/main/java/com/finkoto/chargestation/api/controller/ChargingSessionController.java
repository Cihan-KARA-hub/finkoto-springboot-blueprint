package com.finkoto.chargestation.api.controller;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.services.ChargingSessionService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/charging-Sessions")
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<ChargingSessionDto>> getAllChargingSessions(
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) @ParameterObject Pageable pageable)
    {
        return ResponseEntity.ok(chargingSessionService.getAll(pageable));
    }
    @GetMapping("{id}")
    public ResponseEntity<ChargingSessionDto> getChargeSessionsById(@PathVariable Long id) {
     return ResponseEntity.ok(chargingSessionService.findById(id));
    }

    @PostMapping("/start")
    public ResponseEntity<ChargingSessionDto> start(@RequestBody ChargingSessionDto ChargingSession) {
        //
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/stop")
    public ResponseEntity<ChargingSessionDto> stop(@RequestBody ChargingSessionDto ChargingSession) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
