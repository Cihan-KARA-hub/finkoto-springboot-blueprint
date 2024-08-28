package com.finkoto.ocppmockserver.api.mapper;


import com.finkoto.ocppmockserver.api.dto.ChargeHardwareSpecDto;
import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ChargeHardwareSpecMapper {

    public abstract void toEntity(@MappingTarget ChargeHardwareSpec chargeHardwareSpec, ChargeHardwareSpecDto ChargeHardwareSpecDto);

    public abstract ChargeHardwareSpecDto toDto(ChargeHardwareSpec chargeHardwareSpec);
}
