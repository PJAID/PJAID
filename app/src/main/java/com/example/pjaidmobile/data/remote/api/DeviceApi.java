package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.Device;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeviceApi {
    @GET("/devices/{id}")
    Single<Device> getDeviceById(@Path("id") String id);
}
