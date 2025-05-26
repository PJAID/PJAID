package com.example.pjaidmobile.di;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(SingletonComponent.class)
public class NotificationModule {

    public static final String TICKET_CHANNEL_ID = "ticket_status_channel";

    @Provides
    @Singleton
    @Named("TicketChannelId")
    String provideChannelId() {
        return TICKET_CHANNEL_ID;
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(
            @ApplicationContext Context context) {
        return (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }
}
