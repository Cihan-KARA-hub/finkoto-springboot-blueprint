package com.finkoto.chargestation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.NumericBooleanConverter;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "charge_point")
public class ChargePoint {

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

    @Column(name = "ocpp_id", nullable = false, unique = true)
    private String ocppId;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "online", nullable = false)
    private Boolean online = false;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "disabled", nullable = false)
    private Boolean disabled = false;

    @Column(name = "last_connected")
    private OffsetDateTime lastConnected;

    @Column(name = "last_disconnected")
    private OffsetDateTime lastDisconnected;

    @Column(name = "last_health_checked")
    private OffsetDateTime lastHealthChecked;

    @OneToMany(mappedBy = "chargePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Connector> connectors = new LinkedHashSet<>();

    @OneToOne(mappedBy = "chargePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChargeHardwareSpec chargeHardwareSpec;

    public void addConnector(Connector connector) {
        connector.setChargePoint(this);
        this.connectors.add(connector);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargePoint that = (ChargePoint) o;
        return ocppId.equals(that.ocppId);
    }

    @Override
    public int hashCode() {
        return ocppId.hashCode();
    }
}