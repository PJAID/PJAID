package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String purchaseDate,
            @RequestParam(required = false) String lastService
    ) {
        List<DeviceDto> devices = deviceService.findDevicesByCriteria(name, purchaseDate, lastService);
        return ResponseEntity.ok(devices);
    }
    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto dto) {
        DeviceDto created = deviceService.createDevice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable Long id, @RequestBody DeviceDto dto) {
        return ResponseEntity.ok(deviceService.updateDevice(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
