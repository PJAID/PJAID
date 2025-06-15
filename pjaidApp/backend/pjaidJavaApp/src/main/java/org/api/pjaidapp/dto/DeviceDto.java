package org.api.pjaidapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    private Long id;
    private String name;
    private String serialNumber;
    private String purchaseDate;
    private String lastService;
    private String qrCode;
    private Status status;
    private Double latitude;
    private Double longitude;
    private String statusDevice;
}