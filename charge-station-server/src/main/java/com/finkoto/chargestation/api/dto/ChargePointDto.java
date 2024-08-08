package com.finkoto.chargestation.api.dto;

import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.time.OffsetDateTime;


@Getter
@Setter
public class ChargePointDto implements Serializable {
    private Long id;
    private OffsetDateTime created;
    private OffsetDateTime updated;
    private String ocppId;
    private boolean online;
    private boolean disabled;
    private OffsetDateTime lastConnected;
    private OffsetDateTime lastDisconnected;
    private OffsetDateTime lastHealthChecked;
}