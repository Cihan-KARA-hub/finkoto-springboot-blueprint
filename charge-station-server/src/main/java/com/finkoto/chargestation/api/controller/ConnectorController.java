package com.finkoto.chargestation.api.controller;

import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.services.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/connector")
public class ConnectorController {

     private final ConnectorService connectorService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<ConnectorDto>> getAllConnector(
            //@RequestParam Long chargePointId,
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity.ok(connectorService.getAll(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ConnectorDto> getConnectorById(@PathVariable Long id) {
       return ResponseEntity.ok(connectorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ConnectorDto> createConnector(@RequestBody ConnectorDto connectorDto) {
        connectorService.create(connectorDto);
        return new ResponseEntity<ConnectorDto>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnector(@PathVariable Long id) {
        connectorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<ConnectorDto> updateConnector(@RequestBody ConnectorDto connectorDto) {
        connectorService.update(connectorDto);
        return new ResponseEntity<ConnectorDto>(HttpStatus.NO_CONTENT);
    }
}
