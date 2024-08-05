package com.finkoto.chargestation.services;

import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.api.dto.PageableResponseDto;
import com.finkoto.chargestation.api.mapper.ConnectorMapper;
import com.finkoto.chargestation.model.Connector;
import com.finkoto.chargestation.repository.ConnectorRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectorService {

    private ConnectorRepository connectorRepository;
    private ConnectorMapper connectorMapper;

    @Transactional
    public PageableResponseDto<ConnectorDto> getAll(Pageable pageable) {
        final Page<Connector> pageData = connectorRepository.findAll(pageable);
        final List<ConnectorDto> chargePointDtoList = connectorMapper.toDtoList(pageData.getContent());
        return new PageableResponseDto<>(chargePointDtoList, pageData.getTotalElements(), pageable);
    }
    @Transactional
    public void delete(Long id) {
        connectorRepository.deleteById(id);
    }
    @Transactional
    public ConnectorDto create(ConnectorDto connectorsDto) {
        final Connector connector = connectorMapper.toEntity(connectorsDto);
        return  connectorMapper.toDto( connectorRepository.save(connector));
    }
    @Transactional
    public ConnectorDto findById(Long id) {
        return connectorMapper.toDto( connectorRepository.findById(id).orElse(null));
    }
    @Transactional
    public void update(ConnectorDto connectordto) {
        final Connector connector = connectorMapper.toEntity(connectordto);
        connectorMapper.toDto(connectorRepository.save(connector));
    }
}
