package com.example.pjaidmobile.presentation.features.report;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pjaidmobile.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import jakarta.inject.Named;

@Singleton
public class NotificationService {

    private final NotificationManager notificationManager;
    private final String channelId;
    private final Context appContext;

    @Inject
    public NotificationService(
            NotificationManager notificationManager,
            @Named("TicketChannelId") String channelId,
            @ApplicationContext Context appContext
    ) {
        this.notificationManager = notificationManager;
        this.channelId = channelId;
        this.appContext = appContext;

        createChannelIfNeeded();
    }

    private void createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Ticket Status",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Powiadomienie o zmianie statusu ticketów");
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showTicketStatus(String status) {
        String title = "Zmiana statusu ticketu";
        String message = "Twój ticket zmienił status na \"" + status + "\".";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(appContext).notify((int) System.currentTimeMillis(), builder.build());
    }
}
