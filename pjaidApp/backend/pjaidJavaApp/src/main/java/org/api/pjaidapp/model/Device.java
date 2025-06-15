package org.api.pjaidapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.enums.DeviceStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "purchase_date")
    private String purchaseDate;
    @Column(name = "last_service")
    private String lastService;
    @Column(name = "qr_code")
    private String qrCode;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
    @Enumerated(EnumType.STRING)
    @Column(name = "device_status")
    private DeviceStatus statusDevice;
}
