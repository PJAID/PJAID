package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.exception.DeviceNotFoundException;
import org.api.pjaidapp.mapper.DeviceMapper;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.repository.specification.DeviceSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final QrCodeService qrCodeService;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper, QrCodeService qrCodeService) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.qrCodeService = qrCodeService;
    }

    public DeviceDto getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return deviceMapper.toDto(device);
    }

    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    public DeviceDto createDevice(DeviceDto dto) {
        Device device = deviceMapper.toEntity(dto);
        Device saved = deviceRepository.save(device);
        DeviceDto response = deviceMapper.toDto(saved);

        String url = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/devices/{id}")
                .buildAndExpand(saved.getId())
                .toUriString();

        String qrBase64 = qrCodeService.generatePngBase64(url, 300);
        response.setQrCode(qrBase64);

        return response;
    }

    public DeviceDto updateDevice(Long id, DeviceDto dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        device.setName(dto.getName());
        device.setSerialNumber(dto.getSerialNumber());
        device.setPurchaseDate(dto.getPurchaseDate());
        device.setLastService(dto.getLastService());

        return deviceMapper.toDto(deviceRepository.save(device));
    }

    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.deleteById(id);
    }

    public List<DeviceDto> findDevicesByCriteria(String name, String purchaseDate, String lastService) {
        Specification<Device> spec = DeviceSpecifications.withFilters(name, purchaseDate, lastService);
        List<Device> filteredDevices = deviceRepository.findAll(spec);

        return filteredDevices.stream()
                .map(deviceMapper::toDto)
                .toList();
    }
}
