package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.api.dto.ConnectorDto;
import com.finkoto.ocppmockserver.api.dto.PageableResponseDto;
import com.finkoto.ocppmockserver.api.mapper.ConnectorMapper;
import com.finkoto.ocppmockserver.model.ChargePoint;
import com.finkoto.ocppmockserver.model.Connector;
import com.finkoto.ocppmockserver.model.enums.ConnectorStatus;
import com.finkoto.ocppmockserver.repository.MockChargePointRepository;
import com.finkoto.ocppmockserver.repository.MockConnectorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MockConnectorServices {
    private final MockConnectorRepository mockConnectorRepository;
    private final ConnectorMapper connectorMapper;
    private final MockChargePointRepository mockChargePointRepository;

    @Transactional
    public PageableResponseDto<ConnectorDto> getAll(Pageable pageable, Long chargePointId) {
        Page<Connector> pageData;
        if (chargePointId == null) {
            pageData = mockConnectorRepository.findAll(pageable);
        } else {
            pageData = mockConnectorRepository.findAllByChargePointId(pageable, chargePointId);
        }
        final List<ConnectorDto> chargePointDtoList = connectorMapper.toDtoList(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public void delete(Long id) {
        mockConnectorRepository.deleteById(id);
    }

    @Transactional
    public ConnectorDto create(ConnectorDto connectorDto) {
        Long chargePointId = connectorDto.getChargePointId();
        ChargePoint chargePoint = mockChargePointRepository.findById(chargePointId).orElseThrow(() -> new IllegalStateException("Charge point not found with id: " + chargePointId));
        Connector connector = new Connector();
        connectorMapper.toEntity(connector, connectorDto);
        connector.addChargePoint(chargePoint);
        final Connector newConnector = mockConnectorRepository.save(connector);
        return connectorMapper.toDto(newConnector);
    }

    @Transactional
    public ConnectorDto findById(Long id) {
        return connectorMapper.toDto(mockConnectorRepository.findById(id).orElse(null));
    }

    @Transactional
    public ConnectorDto update(ConnectorDto connectorDto, Long id) {
        final Connector connector = mockConnectorRepository.findById(id).orElseThrow(() -> new IllegalStateException("Connector not found with id: " + id));
        connectorMapper.toEntity(connector, connectorDto);
        final Connector newConnector = mockConnectorRepository.save(connector);
        return connectorMapper.toDto(newConnector);
    }

    @Transactional
    public Long findConnector(Long id) {
        return mockConnectorRepository.findIdByChargePointId(id);
    }

    @Transactional
    public void setStatusCharge(Long id) {
        Connector connector = mockConnectorRepository.findStatusByChargePointId(id);
        if (connector != null) {
            connector.setStatus(ConnectorStatus.Charging);
            mockConnectorRepository.save(connector);
        }
    }
}