package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.Device;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeviceApi {
    @GET("/devices/{id}")
    Call<Device> getDeviceById(@Path("id") String id);
}
