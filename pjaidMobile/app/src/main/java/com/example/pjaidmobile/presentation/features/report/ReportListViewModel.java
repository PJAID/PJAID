package com.example.pjaidmobile.presentation.features.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pjaidmobile.data.model.ReportItem;
import com.example.pjaidmobile.domain.repository.ReportRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ReportListViewModel extends ViewModel {

    private final ReportRepository repository;
    private final MutableLiveData<List<ReportItem>> _reports = new MutableLiveData<>();
    public LiveData<List<ReportItem>> reports = _reports;

    private Disposable disposable; // 🔥 Zarządzanie subskrypcją

    @Inject
    public ReportListViewModel(ReportRepository repository) {
        this.repository = repository;
        fetchReports();
    }

    private void fetchReports() {
        disposable = repository.getAllReports()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        _reports::setValue,
                        throwable -> Log.e("ReportViewModel", "Błąd pobierania raportów", throwable)
                );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose(); // 🔥 Unikamy wycieków pamięci
        }
    }
}