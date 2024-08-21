package com.finkoto.ocppmockserver.model;


import com.finkoto.ocppmockserver.model.enums.ConnectorStatus;
import com.finkoto.ocppmockserver.model.enums.CurrentType;
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
@Table(name = "mock_connector")
public class Connector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, insertable = false, unique = true)
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

    @Column(name = "ocpp_id", nullable = false, columnDefinition = "INT default 1")
    private int ocppId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_type")
    private CurrentType currentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConnectorStatus status;

    @Column(name = "power_factor")
    private Integer powerFactor;

    @ManyToOne
    @JoinColumn(name = "charge_point_id")
    private ChargePoint chargePoint;

    public void addChargePoint(ChargePoint chargePoint) {
        chargePoint.addConnector(this);
        this.chargePoint = chargePoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;
        return ocppId == connector.ocppId && chargePoint.equals(connector.chargePoint);
    }

    @Override
    public int hashCode() {
        int result = ocppId;
        result = 31 * result + chargePoint.hashCode();
        return result;
    }
}
