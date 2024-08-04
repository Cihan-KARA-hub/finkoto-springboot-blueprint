package com.finkoto.chargestation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "charge_central")
public class ChargeCentralModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_central_id")
    private int chargeCentralId; // updated ID field name

    private String country;
    private String state;

    @OneToMany(mappedBy = "chargeStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChargePointModel> chargePoints;
}
