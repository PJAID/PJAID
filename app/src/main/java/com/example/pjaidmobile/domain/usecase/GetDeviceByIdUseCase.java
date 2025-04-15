package com.example.pjaidmobile.domain.usecase;

import android.util.Log;

import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.domain.repository.DeviceRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetDeviceByIdUseCase {
    private static final String TAG = "GetDeviceByIdUseCase";
    private final DeviceRepository deviceRepository;

    @Inject
    public GetDeviceByIdUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        Log.d(TAG, "Use case initialized with DeviceRepository");
    }

    public Single<Device> execute(String deviceId) {
        Log.d(TAG, "Executing use case with deviceId: " + deviceId);

        if (deviceId == null || deviceId.trim().isEmpty()) {
            Log.e(TAG, "Invalid deviceId: must not be null or empty");
            return Single.error(new IllegalArgumentException("Device ID must not be empty"));
        }

        return deviceRepository.getDeviceById(deviceId)
                .doOnSubscribe(disposable -> Log.d(TAG, "Fetching device with ID: " + deviceId))
                .doOnSuccess(device -> Log.i(TAG, "Device fetched successfully: " + device))
                .doOnError(throwable -> Log.e(TAG, "Failed to fetch device: " + throwable.getMessage(), throwable));
    }
}
