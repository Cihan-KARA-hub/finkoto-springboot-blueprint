package com.finkoto.chargestation.api.dto;

import com.finkoto.chargestation.model.enums.ConnectorStatus;
import com.finkoto.chargestation.model.enums.CurrentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class ConnectorDto {
    private Long id;
    private int version;
    private OffsetDateTime created;
    private OffsetDateTime updated;
    private int ocppId;
    private CurrentType currentType;
    private ConnectorStatus status;
    private Integer powerFactor;
    private Long chargePointId;
}
