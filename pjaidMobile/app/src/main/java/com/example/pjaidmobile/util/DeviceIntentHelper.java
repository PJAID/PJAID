package com.example.pjaidmobile.util;

import android.content.Context;
import android.content.Intent;

import com.example.pjaidmobile.data.model.Device;

public class DeviceIntentHelper {

    public static final String EXTRA_DEVICE_NAME = "device_name";
    public static final String EXTRA_DEVICE_SERIAL = "device_serial";
    public static final String EXTRA_DEVICE_PURCHASE = "device_purchase";

    private DeviceIntentHelper() {
    }

    public static Intent createIntentWithDevice(Context context, Class<?> targetActivity, Device device) {
        Intent intent = new Intent(context, targetActivity);

        if (device.getName() != null) {
            intent.putExtra(EXTRA_DEVICE_NAME, device.getName());
        }
        if (device.getSerialNumber() != null) {
            intent.putExtra(EXTRA_DEVICE_SERIAL, device.getSerialNumber());
        }
        if (device.getPurchaseDate() != null) {
            intent.putExtra(EXTRA_DEVICE_PURCHASE, device.getPurchaseDate());
        }

        return intent;
    }

    public static Device extractDeviceFromIntent(Intent intent) {
        String name = intent.getStringExtra(EXTRA_DEVICE_NAME);
        String serial = intent.getStringExtra(EXTRA_DEVICE_SERIAL);
        String purchase = intent.getStringExtra(EXTRA_DEVICE_PURCHASE);

        Device device = new Device();
        device.setName(name);
        device.setSerialNumber(serial);
        device.setPurchaseDate(purchase);
        return device;
    }
}
