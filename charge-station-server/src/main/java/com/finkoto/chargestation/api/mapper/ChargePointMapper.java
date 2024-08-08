package com.finkoto.chargestation.api.mapper;

import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.api.dto.ChargingSessionDto;
import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.model.ChargingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ChargePointMapper {

    public abstract ChargePointDto toDto(ChargePoint chargePoint);

    public abstract List<ChargePointDto> toDto(List<ChargePoint> chargePoint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "online", ignore = true)
    @Mapping(target = "lastConnected", ignore = true)
    @Mapping(target = "lastDisconnected", ignore = true)
    @Mapping(target = "lastHealthChecked", ignore = true)
    @Mapping(target = "connectors", ignore = true)
    public abstract void toEntity(@MappingTarget ChargePoint chargePoint, ChargePointDto chargePointDto);
}
