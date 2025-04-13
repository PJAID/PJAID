package com.example.pjaidmobile.presentation.features.report;

public interface ReportUiState {


    class Idle implements ReportUiState {
        private Idle() {
        }
    }

    class Loading implements ReportUiState {
        private Loading() {
        }
    }

    class Success implements ReportUiState {
        private Success() {
        }
    }

    class Error implements ReportUiState {
        public final String message;

        public Error(String message) {
            this.message = message;
        }
    }

    class InitialData implements ReportUiState {
        public final String deviceInfo;

        public InitialData(String deviceInfo) {
            this.deviceInfo = deviceInfo;
        }
    }

    ReportUiState IDLE = new Idle();
    ReportUiState LOADING = new Loading();
    ReportUiState SUCCESS = new Success();
}
