package com.finkoto.ocppmockserver.model;

import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "mock_charging_session")
public class MockChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
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
    private String unit = "Wh";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8, nullable = false)
    private SessionStatus status = SessionStatus.NEW;

    @Column(name = "begin_time")
    private OffsetDateTime beginTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;

    @Column(name = "battery_percentage_start", length = 50)
    private String batteryPercentageStart = "0";

    @Column(name = "battery_percentage", length = 50)
    private String batteryPercentage;

    @Column(name = "consumption", precision = 15, scale = 6)
    private BigDecimal consumption;

    @Column(name = "active_power", precision = 15, scale = 2)
    private BigDecimal activePower;

    @Column(name = "active_power_unit", length = 30)
    private String activePowerUnit = "Wh";

    @Column(name = "unplug_time")
    private LocalDateTime unplugTime;

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

        MockChargingSession that = (MockChargingSession) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
