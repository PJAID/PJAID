package com.example.pjaidmobile.data.repository;

import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.data.remote.api.DeviceApi;
import com.example.pjaidmobile.domain.repository.DeviceRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class DeviceRepositoryImpl implements DeviceRepository {

    private final DeviceApi deviceApi;

    @Inject
    public DeviceRepositoryImpl(DeviceApi deviceApi) {
        this.deviceApi = deviceApi;
    }

    @Override
    public Single<Device> getDeviceById(String id) {
        return deviceApi.getDeviceById(id);
    }
}
