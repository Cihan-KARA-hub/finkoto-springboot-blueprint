package com.finkoto.chargestation.api.mapper;

import com.finkoto.chargestation.api.dto.ChargePointDto;
import com.finkoto.chargestation.model.ChargePoint;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ChargePointMapper {

    public abstract ChargePointDto toDto(ChargePoint chargePoint);

    public abstract List<ChargePointDto> toDto(List<ChargePoint> chargePoint);

    public abstract ChargePoint toEntity(ChargePointDto chargePointDto);


}
