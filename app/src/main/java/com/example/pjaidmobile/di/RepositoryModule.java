package com.example.pjaidmobile.di;

import com.example.pjaidmobile.data.repository.DeviceRepositoryImpl;
import com.example.pjaidmobile.data.repository.ReportRepositoryImpl;
import com.example.pjaidmobile.domain.repository.DeviceRepository;
import com.example.pjaidmobile.domain.repository.ReportRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract ReportRepository bindReportRepository(
            ReportRepositoryImpl reportRepositoryImpl
    );

    @Binds
    @Singleton
    public abstract DeviceRepository bindDeviceRepository(
            DeviceRepositoryImpl deviceRepositoryImpl
    );

}
