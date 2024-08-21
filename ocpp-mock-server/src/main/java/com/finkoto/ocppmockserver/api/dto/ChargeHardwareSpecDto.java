package com.finkoto.ocppmockserver.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChargeHardwareSpecDto {


    private Long id;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String chargePointVendor;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String chargePointModel;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String firmwareVersion;

    @NotEmpty
    @Size(min = 1, max = 25)
    private String chargePointSerialNumber;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String meterType;

    @NotEmpty
    @Size(min = 1, max = 25)
    private String meterSerialNumber;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String imsi;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String iccid;

    @NotEmpty
    @Size(min = 1, max = 25)
    private String chargeBoxSerialNumber;

}