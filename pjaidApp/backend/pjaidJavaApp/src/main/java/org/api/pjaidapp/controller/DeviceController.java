package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/devices")
public class DeviceController {


    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceService deviceService, DeviceRepository deviceRepository) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
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
    @GetMapping("/status-summary")
    public Map<String, Long> getStatusSummary() {
        List<Device> allDevices = deviceRepository.findAll();
        return allDevices.stream()
                .collect(Collectors.groupingBy(d -> d.getStatus().name(), Collectors.counting()));
    }
}
