package com.finkoto.ocppmockserver.services;

import com.finkoto.ocppmockserver.model.Connector;
import com.finkoto.ocppmockserver.model.enums.ConnectorStatus;
import com.finkoto.ocppmockserver.repository.ConnectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConnectorServices {
    private final ConnectorRepository connectorRepository;

    public Long findConnector(Long id) {
        return connectorRepository.findIdByChargePointId(id);
    }

    public void setStatusCharge(Long id) {
        Connector connector = connectorRepository.findStatusByChargePointId(id);
        if (connector != null) {
            connector.setStatus(ConnectorStatus.Charging);
            connectorRepository.save(connector);
        }
    }
}
