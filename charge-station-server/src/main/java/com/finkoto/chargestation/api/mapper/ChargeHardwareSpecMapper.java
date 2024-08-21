package com.finkoto.chargestation.api.mapper;


import com.finkoto.chargestation.api.dto.ChargeHardwareSpecDto;
import com.finkoto.chargestation.model.ChargeHardwareSpec;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ChargeHardwareSpecMapper {
    public abstract void toEntity(@MappingTarget ChargeHardwareSpec chargeHardwareSpec, ChargeHardwareSpecDto ChargeHardwareSpecDto);

}
