package com.example.pjaidmobile.presentation.features.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pjaidmobile.domain.usecase.GetDeviceByIdUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

@HiltViewModel
public class ScanQRViewModel extends ViewModel {

    private final GetDeviceByIdUseCase getDeviceByIdUseCase;
    private final MutableLiveData<ScanUiState> _uiState = new MutableLiveData<>(ScanUiState.IDLE);
    public final LiveData<ScanUiState> uiState = _uiState;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ScanQRViewModel(GetDeviceByIdUseCase getDeviceByIdUseCase) {
        this.getDeviceByIdUseCase = getDeviceByIdUseCase;
    }

    public void fetchDevice(String scannedId) {
        _uiState.setValue(ScanUiState.FETCHING_DEVICE);
        disposables.add(
                getDeviceByIdUseCase.execute(scannedId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                device -> _uiState.setValue(new ScanUiState.DeviceFound(device)), // onSuccess
                                throwable -> { // onError
                                    if (throwable instanceof HttpException && ((HttpException) throwable).code() == 404) {
                                        _uiState.setValue(ScanUiState.DEVICE_NOT_FOUND);
                                    } else {
                                        _uiState.setValue(new ScanUiState.Error(
                                                throwable.getMessage() != null ? throwable.getMessage() : "Nieznany błąd API"
                                        ));
                                    }
                                }
                        )
        );
    }

    public void scanCancelled() {
        _uiState.setValue(ScanUiState.SCAN_CANCELLED);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}