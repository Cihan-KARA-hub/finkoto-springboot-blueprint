package com.finkoto.chargestation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @Column(name = "ocpp_id", nullable = false, unique = true, length = 255)
    private String ocppId;

    @Column(name = "online", nullable = false, columnDefinition = "bit default b'1'")
    private boolean online;

    @Column(name = "disabled", nullable = false, columnDefinition = "bit default b'0'")
    private boolean disabled;

    @Column(name = "last_connected")
    private OffsetDateTime lastConnected;

    @Column(name = "last_disconnected")
    private OffsetDateTime lastDisconnected;

    @Column(name = "last_health_checked")
    private OffsetDateTime lastHealthChecked;

    @OneToMany(mappedBy = "chargePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Connector> connectors = new LinkedHashSet<>();

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

