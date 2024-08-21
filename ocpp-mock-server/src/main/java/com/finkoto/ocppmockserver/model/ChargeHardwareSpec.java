package com.finkoto.ocppmockserver.model;


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
@Table(name = "mock_charge_hardware_spec")
public class ChargeHardwareSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "charge_point_vendor", nullable = false, length = 20)
    private String chargePointVendor;

    @Column(name = "charge_point_model", nullable = false, length = 20)
    private String chargePointModel;

    @Column(name = "charge_point_serial_number", nullable = false, length = 25)
    private String chargePointSerialNumber;

    @Column(name = "charge_box_serial_number", nullable = false, length = 20)
    private String chargeBoxSerialNumber;

    @Column(name = "firmware_version", nullable = false, length = 25)
    private String firmwareVersion;

    @Column(name = "iccid", nullable = false, length = 20)
    private String iccid;

    @Column(name = "imsi", nullable = false, length = 20)
    private String imsi;

    @Column(name = "meter_type", nullable = false, length = 50)
    private String meterType;

    @Column(name = "meter_serial_number", nullable = false, length = 25)
    private String meterSerialNumber;

    @OneToOne
    @JoinColumn(name = "charge_point_id", referencedColumnName = "id", unique = true)
    private ChargePoint chargePoint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChargeHardwareSpec that = (ChargeHardwareSpec) o;
        return chargePoint.equals(that.chargePoint);
    }

    @Override
    public int hashCode() {
        return chargePoint.hashCode();
    }


}
