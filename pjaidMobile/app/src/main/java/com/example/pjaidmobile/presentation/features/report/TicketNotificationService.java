package com.example.pjaidmobile.presentation.features.report;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pjaidmobile.R;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class TicketNotificationService {

    private static final String CHANNEL_ID = "ticket_channel";
    private static final String CHANNEL_NAME = "Ticket Updates";

    private final Context context;

    @Inject
    public TicketNotificationService(@ApplicationContext Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void showStatusChanged(String status) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Status zgłoszenia")
                .setContentText("Twój ticket zmienił status na: " + status)
                .setSmallIcon(R.drawable.icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(status.hashCode(), builder.build());
    }
}
