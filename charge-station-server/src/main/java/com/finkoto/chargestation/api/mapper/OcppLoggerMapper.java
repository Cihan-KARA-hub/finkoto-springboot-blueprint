package com.finkoto.chargestation.api.mapper;


import com.finkoto.chargestation.api.dto.OcppLoggerDto;
import com.finkoto.chargestation.model.OcppLogger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OcppLoggerMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    public abstract OcppLogger  toEntity(@MappingTarget OcppLogger ocppLogger, OcppLoggerDto ocppLoggerDto);
}
