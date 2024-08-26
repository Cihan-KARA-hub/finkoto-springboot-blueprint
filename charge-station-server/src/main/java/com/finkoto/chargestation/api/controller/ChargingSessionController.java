package com.finkoto.chargestation.api.controller;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.ocpp.OCPPCentralSystem;
import com.finkoto.chargestation.services.ChargingSessionService;
import jakarta.validation.Valid;
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
    private final OCPPCentralSystem centralSystem;


    @GetMapping
    public ResponseEntity<PageableResponseDto<ChargingSessionDto>> getAllChargingSessions(@Valid @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(chargingSessionService.getAll(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ChargingSessionDto> getChargeSessionsById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(chargingSessionService.findById(id));
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(@Valid @RequestParam String ocppId, @RequestParam int connectorId, @RequestParam String idTag) {
        centralSystem.sendRemoteStartTransactionRequest(ocppId, connectorId, idTag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/stop/{idTag}")
    public ResponseEntity<Void> stop(@Valid @RequestParam String ocppId, @RequestParam int connectorId, @PathVariable String idTag) {
        chargingSessionService.sendRemoteStopTransactionRequest(ocppId, connectorId, idTag);
        return ResponseEntity.ok().build();
    }
}
