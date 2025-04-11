package org.api.pjaidapp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private String id;
    private String name;
    private String serialNumber;
    private String purchaseDate;
    private String lastService;
}
