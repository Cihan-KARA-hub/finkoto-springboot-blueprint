package com.finkoto.ocppmockserver.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mock_ocpp_logger")
public class OcppLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @org.springframework.data.annotation.Version
    @Column(name = "version", nullable = false)
    private int version;

    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private OffsetDateTime created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private OffsetDateTime updated;

    @Column(name = "charge_point_ocpp_id", length = 20)
    private String chargePointOcppId;

    @Column(name = "connector_ocpp_id", length = 20)
    private String connectorOcppId;

    @Column(name = "charging_session_id", length = 20)
    private String chargingSessionId;

    @Column(name = "info", length = 100)
    private String info;

}
