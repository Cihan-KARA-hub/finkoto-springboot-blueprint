package com.finkoto.chargestation.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class OcppLoggerDto {

    private Long id;

    private int version;

    private OffsetDateTime created;

    private OffsetDateTime updated;

    private String chargePointOcppId;

    private String connectorOcppId;

    private String chargingSessionId;

    private String info;

}
