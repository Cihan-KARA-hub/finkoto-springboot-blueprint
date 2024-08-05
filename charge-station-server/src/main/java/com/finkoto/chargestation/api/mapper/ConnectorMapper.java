package com.finkoto.chargestation.api.mapper;



import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.model.Connector;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ConnectorMapper {

    public abstract ConnectorDto toDto(Connector connector);

    public abstract List<ConnectorDto> toDtoList(List<Connector> connectorList);

    public abstract Connector toEntity(ConnectorDto connectorDto);
}
