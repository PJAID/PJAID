package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.exception.DeviceNotFoundException;
import org.api.pjaidapp.mapper.DeviceMapper;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.repository.specification.DeviceSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
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
        return deviceMapper.toDto(saved);
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
        // Utwórz specyfikację na podstawie filtrów
        Specification<Device> spec = DeviceSpecifications.withFilters(name, purchaseDate, lastService);

        // Pobierz przefiltrowane encje z repozytorium
        List<Device> filteredDevices = deviceRepository.findAll(spec);

        // Zmapuj encje na DTO
        return filteredDevices.stream()
                .map(deviceMapper::toDto) // Użyj swojej metody mapującej
                .toList();
    }
}
