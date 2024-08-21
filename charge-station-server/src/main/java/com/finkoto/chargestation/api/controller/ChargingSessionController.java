package com.finkoto.chargestation.api.controller;

import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.ocpp.OCPPCentralSystem;
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
    private final OCPPCentralSystem centralSystem;


    @GetMapping
    public ResponseEntity<PageableResponseDto<ChargingSessionDto>> getAllChargingSessions(
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(chargingSessionService.getAll(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ChargingSessionDto> getChargeSessionsById(@PathVariable Long id) {
        return ResponseEntity.ok(chargingSessionService.findById(id));
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestParam String ocppId, @RequestParam int connectorId, @RequestParam String userId) {
        centralSystem.sendRemoteStartTransactionRequest(ocppId, connectorId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/stop")
    public ResponseEntity<ChargingSessionDto> stop(@RequestParam String occpId,@RequestParam int connectorId) {
        chargingSessionService.remoteStop(occpId,connectorId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
