package com.finkoto.chargestation.model;


import com.finkoto.chargestation.model.enums.Reason;
import com.finkoto.chargestation.model.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "charging_session")
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "int default 1")
    private int version;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private OffsetDateTime created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private OffsetDateTime updated;

    @Column(name = "meter_start")
    private Integer meterStart = 0;

    @Column(name = "curr_meter", length = 50)
    private String currMeter;

    @Column(name = "meter_stop")
    private Integer meterStop;

    @Column(name = "unit", length = 30)
    private String unit ="Wh";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum('FAILED','FINISHED','ACTIVE','CANCELED','NEW') default 'NEW'")
    private SessionStatus status = SessionStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private Reason reason;

    @Column(name = "begin_time")
    private OffsetDateTime beginTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;

    @Column(name = "battery_percentage_start", length = 50)
    private String batteryPercentageStart;

    @Column(name = "battery_percentage", length = 50)
    private String batteryPercentage;

    @Column(name = "consumption", precision = 15, scale = 6)
    private BigDecimal consumption;

    @Column(name = "active_power", precision = 15, scale = 2)
    private BigDecimal activePower;

    @Column(name = "active_power_unit", length = 30)
    private String activePowerUnit;

    @Column(name = "unplug_time")
    private OffsetDateTime unplugTime;

    /*@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "id")
    private Connector connector;*/

    @Column(name = "charge_point_ocpp_id")
    private String chargePointOcppId;

    @Column(name = "connector_id")
    private int connectorId;

    @Column(name = "id_tag")
    private String idTag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargingSession that = (ChargingSession) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

