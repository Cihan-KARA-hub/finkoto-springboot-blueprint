package com.finkoto.ocppmockserver.api.mapper;


import com.finkoto.ocppmockserver.api.dto.ChargeHardwareSpecDto;
import com.finkoto.ocppmockserver.api.dto.ChargePointDto;
import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
import com.finkoto.ocppmockserver.model.ChargePoint;
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
    @Mapping(target = "lastConnected", ignore = true)
    @Mapping(target = "lastDisconnected", ignore = true)
    @Mapping(target = "lastHealthChecked", ignore = true)
    public abstract void toEntity(@MappingTarget ChargePoint chargePoint, ChargePointDto chargePointDto);

    @Mapping(target = "id", ignore = true)
    public abstract void toEntity(@MappingTarget ChargeHardwareSpec chargeHardwareSpec, ChargeHardwareSpecDto ChargeHardwareSpecDto);

    @AfterMapping
    public void afterToEntityMapping(@MappingTarget ChargePoint chargePoint) {
        Optional.ofNullable(chargePoint.getChargeHardwareSpec()).ifPresent(chargeHardwareSpec -> chargeHardwareSpec.setChargePoint(chargePoint));
    }

}
