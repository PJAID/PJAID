package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.Device;

import io.reactivex.rxjava3.core.Single;

public interface DeviceRepository {
    Single<Device> getDeviceById(String id);
}
