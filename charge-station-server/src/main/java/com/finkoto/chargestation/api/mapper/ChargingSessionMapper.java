package com.finkoto.chargestation.api.mapper;


import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.model.ChargingSession;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ChargingSessionMapper {

    public abstract ChargingSessionDto toDto(ChargingSession session);

    public abstract List<ChargingSessionDto> toDtoList(List<ChargingSession> session);

    public abstract ChargingSession toEntity(ChargingSessionDto dto);

}
