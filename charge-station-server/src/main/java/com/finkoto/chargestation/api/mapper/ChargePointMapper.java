package com.finkoto.chargestation.api.mapper;

import com.finkoto.chargestation.api.dto.ChargeHardwareSpecDto;
import com.finkoto.chargestation.api.dto.ChargePointDto;

import com.finkoto.chargestation.api.dto.ConnectorDto;
import com.finkoto.chargestation.model.ChargeHardwareSpec;
import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.model.Connector;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;


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
    public abstract void toEntity(@MappingTarget ChargePoint chargePoint, ChargePointDto chargePointDto);

    @Mapping(target = "id", ignore = true)
    public abstract void toEntity(@MappingTarget ChargeHardwareSpec chargeHardwareSpec, ChargeHardwareSpecDto ChargeHardwareSpecDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    public abstract void toEntity(@MappingTarget Connector connector , ConnectorDto connectorDto);

    @AfterMapping
    public void afterToEntityMapping(@MappingTarget ChargePoint chargePoint){
        Optional.ofNullable(chargePoint.getChargeHardwareSpec()).ifPresent(chargeHardwareSpec -> chargeHardwareSpec.setChargePoint(chargePoint));
    }
}
