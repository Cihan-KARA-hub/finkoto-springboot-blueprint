package com.finkoto.chargestation.model;

import com.finkoto.chargestation.model.enums.CurrentType;
import com.finkoto.chargestation.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connector")
public class Connector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "int default 1")
    private int version;

    @CreatedDate
    @Column(name = "created", nullable = false)
    private OffsetDateTime created;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private OffsetDateTime updated;

    @Column(name = "ocpp_id", nullable = false,  columnDefinition = "INT default 1")
    private int ocppId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_type")
    private CurrentType currentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "power_factor")
    private Integer powerFactor;

    @ManyToOne
    @JoinColumn(name = "charge_point_id")
    private ChargePoint chargePoint;

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
