package com.finkoto.chargestation.api.dto;

import com.finkoto.chargestation.model.enums.SessionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class IdTagInfoDto {
    private OffsetDateTime expiryDate;
    private String parentIdTag;
    private SessionStatus status;
    private int transactionId;
    private Object completedHandler;
}
