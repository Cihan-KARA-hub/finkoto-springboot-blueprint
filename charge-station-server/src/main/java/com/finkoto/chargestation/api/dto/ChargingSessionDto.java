package com.finkoto.chargestation.api.dto;

import com.finkoto.chargestation.model.enums.Reason;
import com.finkoto.chargestation.model.enums.SessionStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ChargingSessionDto {
    private Long id;
    private int version;
    private OffsetDateTime created;
    private OffsetDateTime updated;
    private Integer meterStart;
    private String currMeter;
    private Integer meterStop;
    private String unit;
    private OffsetDateTime beginTime;
    private OffsetDateTime endTime;
    private String batteryPercentageStart;
    private String batteryPercentage;
    private BigDecimal consumption;
    private BigDecimal activePower;
    private String activePowerUnit;
    private OffsetDateTime unplugTime;
    private Reason reason;
    private SessionStatus status;
}