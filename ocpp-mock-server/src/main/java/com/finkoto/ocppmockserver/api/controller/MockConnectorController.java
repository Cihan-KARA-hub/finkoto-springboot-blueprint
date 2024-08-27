package com.finkoto.ocppmockserver.api.controller;

import com.finkoto.ocppmockserver.api.dto.ConnectorDto;
import com.finkoto.ocppmockserver.api.dto.PageableResponseDto;
import com.finkoto.ocppmockserver.services.MockConnectorServices;
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
@RequestMapping("/mock_connectors")
public class MockConnectorController {

    private final MockConnectorServices connectorService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<ConnectorDto>> getAllConnector(
            @RequestParam(required = false) Long chargePointId,
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(connectorService.getAll(pageable, chargePointId));
    }
    @PostMapping
    public ResponseEntity<ConnectorDto> createConnector(@RequestBody ConnectorDto connectorDto) {
        connectorService.create(connectorDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConnectorDto> updateConnector(@RequestBody ConnectorDto connectorDto, @PathVariable Long id) {
        return ResponseEntity.ok(connectorService.update(connectorDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnector(@PathVariable Long id) {
        connectorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
