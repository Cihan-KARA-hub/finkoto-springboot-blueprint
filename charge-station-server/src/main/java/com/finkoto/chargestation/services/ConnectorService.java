package com.finkoto.chargestation.services;

import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ConnectorMapper;
import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.model.Connector;
import com.finkoto.chargestation.model.enums.ConnectorStatus;
import com.finkoto.chargestation.repository.ChargePointRepository;
import com.finkoto.chargestation.repository.ConnectorRepository;
import eu.chargetime.ocpp.model.core.ChargePointStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConnectorService {
    private final ConnectorRepository connectorRepository;
    private final ConnectorMapper connectorMapper;
    private final ChargePointRepository chargePointRepository;


    @Transactional
    public PageableResponseDto<ConnectorDto> getAll(Pageable pageable, Long chargePointId) {
        Page<Connector> pageData;
        if (chargePointId == null) {
            pageData = connectorRepository.findAll(pageable);
        } else {
            pageData = connectorRepository.findAllByChargePointId(pageable, chargePointId);
        }
        final List<ConnectorDto> chargePointDtoList = connectorMapper.toDtoList(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }

    @Transactional
    public void delete(Long id) {
        connectorRepository.deleteById(id);
    }

    @Transactional
    public ConnectorDto create(ConnectorDto connectorDto) {
        Long chargePointId = connectorDto.getChargePointId();
        ChargePoint chargePoint = chargePointRepository.findById(chargePointId).orElseThrow(() -> new IllegalStateException("Charge point not found with id: " + chargePointId));
        Connector connector = new Connector();
        connectorMapper.toEntity(connector, connectorDto);
        connector.addChargePoint(chargePoint);
        connector = connectorRepository.save(connector);
        return connectorMapper.toDto(connector);
    }

    @Transactional
    public ConnectorDto findById(Long id) {
        return connectorMapper.toDto(connectorRepository.findById(id).orElse(null));
    }

    @Transactional
    public ConnectorDto update(ConnectorDto connectorDto, Long id) {
        final Connector connector = connectorRepository.findById(id).orElseThrow(() -> new IllegalStateException("Connector not found with id: " + id));
        connectorMapper.toEntity(connector, connectorDto);
        final Connector newConnector = connectorRepository.save(connector);
        return connectorMapper.toDto(newConnector);
    }

    @Transactional
    public void handleStatusNotificationUpdate(ChargePointStatus status, Integer connectorId) {
       Optional<Connector> connector = connectorRepository.findById(Long.valueOf(connectorId));
      connector.ifPresent(connector1 -> {
          connector1.setStatus(ConnectorStatus.valueOf(status.toString()));
          connectorRepository.save(connector1);
      });
    }


//    @Transactional
//    public boolean statusActivateNewChargingSession(int id) {
//        Long idx = (long) id;
//        Connector connector = connectorRepository.findById(idx).orElseThrow(() -> new IllegalStateException("Connector not found with id: " + id));
//        if (connector.getStatus() == ConnectorStatus.Finishing || connector.getStatus() == ConnectorStatus.Available || connector.getStatus() == ConnectorStatus.Preparing) {
//            connector.setStatus(ConnectorStatus.Charging);
//            connectorRepository.save(connector);
//            return true;
//        } else {
//            return false;
//        }
//    }
/*
    @Transactional
    public boolean statusHandleStopTransactionRequest(int id) {
        Long idx = (long) id;
        Connector connector = connectorRepository.findById(idx).orElseThrow(() -> new IllegalStateException("Connector not found with id: " + id));
        if (connector.getStatus() == ConnectorStatus.Charging) {
            connector.setStatus(ConnectorStatus.Finishing);
            connectorRepository.save(connector);
            return true;
        } else {
            connector.setStatus(ConnectorStatus.Faulted);
            connectorRepository.save(connector);
            return false;
        }
    }
*/
}
