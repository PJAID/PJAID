package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsBySerialNumber(String serialNumber);
}