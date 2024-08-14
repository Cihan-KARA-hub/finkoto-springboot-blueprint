package com.finkoto.chargestation.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartRequestDto {
    private int connectorId;
    private String occpId;
    private String userId;
}
