package com.finkoto.chargestation.api.mapper;


import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.model.ChargingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ChargingSessionMapper {

    public abstract ChargingSessionDto toDto(ChargingSession session);

    public abstract List<ChargingSessionDto> toDtoList(List<ChargingSession> session);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    public abstract ChargingSession toEntity(@MappingTarget ChargingSession chargingSession, ChargingSessionDto chargingSessionDto);
}
