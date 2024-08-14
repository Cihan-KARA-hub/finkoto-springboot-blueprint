package com.finkoto.chargestation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChargeHardwareSpecDto {

    private Long id;
    private String chargePointVendor;
    private String chargePointModel;
    private String chargePointSerialNumber;
    private String meterType;
    private String meterSerialNumber;

}