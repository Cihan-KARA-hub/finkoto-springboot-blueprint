package com.finkoto.ocppmockserver.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class ChargePointDto {
    private Long id;

    private OffsetDateTime created;

    private OffsetDateTime updated;

    private String ocppId;

    private boolean online;

    private boolean disabled;

    private OffsetDateTime lastConnected;

    private OffsetDateTime lastDisconnected;

    private OffsetDateTime lastHealthChecked;

    private ChargeHardwareSpecDto chargeHardwareSpec;
}