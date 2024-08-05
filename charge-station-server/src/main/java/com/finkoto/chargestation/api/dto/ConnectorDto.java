package com.finkoto.chargestation.api.dto;

import com.finkoto.chargestation.model.ChargePoint;
import com.finkoto.chargestation.model.enums.CurrentType;
import com.finkoto.chargestation.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;


@Getter
@Setter
public class ConnectorDto implements Serializable {

    private Long id;
    private int version;
    private OffsetDateTime created;
    private OffsetDateTime updated;
    private int ocppId;
    private CurrentType currentType;
    private Status status;
    private Integer powerFactor;
    private ChargePoint chargePoint;

}
