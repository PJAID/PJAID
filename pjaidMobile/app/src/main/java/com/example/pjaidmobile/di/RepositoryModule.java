package com.example.pjaidmobile.di;

import com.example.pjaidmobile.data.repository.AuthRepositoryImpl;
import com.example.pjaidmobile.data.repository.DeviceRepositoryImpl;
import com.example.pjaidmobile.data.repository.ReportRepositoryImpl;
import com.example.pjaidmobile.data.repository.TicketRepositoryImpl;
import com.example.pjaidmobile.domain.repository.AuthRepository;
import com.example.pjaidmobile.domain.repository.DeviceRepository;
import com.example.pjaidmobile.domain.repository.ReportRepository;
import com.example.pjaidmobile.domain.repository.TicketRepository;
import com.example.pjaidmobile.util.LocationProvider;
import com.example.pjaidmobile.util.LocationProviderImpl;

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

    @Binds
    @Singleton
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract LocationProvider bindLocationProvider(LocationProviderImpl impl);

    @Binds
    @Singleton
    public abstract TicketRepository bindTicketRepository(TicketRepositoryImpl impl);

}
