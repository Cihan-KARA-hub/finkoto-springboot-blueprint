package com.finkoto.chargestation.api.mapper;


import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.model.Connector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ConnectorMapper {

    public abstract ConnectorDto toDto(Connector connector);

    public abstract List<ConnectorDto> toDtoList(List<Connector> connectorList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    public abstract void toEntity(@MappingTarget  Connector connector, ConnectorDto connectorDto);


}
