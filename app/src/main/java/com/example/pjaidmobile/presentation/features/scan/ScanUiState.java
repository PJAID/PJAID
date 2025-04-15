package com.example.pjaidmobile.presentation.features.scan;

import com.example.pjaidmobile.data.model.Device;

public interface ScanUiState {
    class Idle implements ScanUiState {
        private Idle() {
        }
    }

    class Scanning implements ScanUiState {
        private Scanning() {
        }
    }

    class FetchingDevice implements ScanUiState {
        private FetchingDevice() {
        }
    }

    class ScanCancelled implements ScanUiState {
        private ScanCancelled() {
        }
    }

    class DeviceFound implements ScanUiState {
        public final Device device; // Lub DomainDevice

        public DeviceFound(Device device) {
            this.device = device;
        }
    }

    class DeviceNotFound implements ScanUiState {
        private DeviceNotFound() {
        }
    }

    class Error implements ScanUiState {
        public final String message;

        public Error(String message) {
            this.message = message;
        }
    }

    ScanUiState IDLE = new Idle();
    ScanUiState SCANNING = new Scanning();
    ScanUiState FETCHING_DEVICE = new FetchingDevice();
    ScanUiState SCAN_CANCELLED = new ScanCancelled();
    ScanUiState DEVICE_NOT_FOUND = new DeviceNotFound();
}
