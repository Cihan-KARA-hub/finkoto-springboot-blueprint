package com.finkoto.chargestation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "charge_point")
public class ChargePointModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_point_id")
    private int chargePointId;

    private int connector; // corrected typo
    private boolean disable;
    private int ocppId; // corrected typo

    @ManyToOne
    @JoinColumn(name = "charge_station_id")
    private ChargeCentralModel chargeStation;
}

