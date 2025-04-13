package org.api.pjaidapp.controller;

import org.api.pjaidapp.model.Device;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
        Device device = new Device();
        device.setId(id);
        device.setName("Laptop Dell Latitude");
        device.setSerialNumber("DL123456");
        device.setPurchaseDate("2023-01-15");
        device.setLastService("2024-12-20");

        return ResponseEntity.ok(device);
    }
}
