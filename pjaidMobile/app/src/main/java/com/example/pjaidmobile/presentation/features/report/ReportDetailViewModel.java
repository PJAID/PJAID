package com.example.pjaidmobile.presentation.features.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pjaidmobile.data.model.ReportItem;
import com.example.pjaidmobile.domain.repository.ReportRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ReportDetailViewModel extends ViewModel {

    private final ReportRepository repository;
    private final MutableLiveData<ReportItem> report = new MutableLiveData<>();
    public LiveData<ReportItem> getReport() {
        return report;
    }

    private Disposable disposable;

    @Inject
    public ReportDetailViewModel(ReportRepository repository) {
        this.repository = repository;
    }

    public void loadReportById(String id) {
        disposable = repository.getReportById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(report::setValue,
                        throwable -> {
                            // Log error
                        });
    }

    @Override
    protected void onCleared() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onCleared();
    }
}
