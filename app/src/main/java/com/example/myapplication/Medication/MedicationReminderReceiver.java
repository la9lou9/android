package com.example.myapplication.Medication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

public class MedicationReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "medication_reminder_channel";
    private static final String CHANNEL_NAME = "Medication Reminders";
    private static final String CHANNEL_DESC = "Notifications for medication reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract medication details from intent
        long medicationId = intent.getLongExtra("MEDICATION_ID", -1);
        String medicationName = intent.getStringExtra("MEDICATION_NAME");
        String dosage = intent.getStringExtra("DOSAGE");

        if (medicationId == -1 || medicationName == null) {
            return;
        }

        // Create an intent to open the app when notification is tapped
        Intent notificationIntent = new Intent(context, MedicationListActivity.class);
        notificationIntent.putExtra("MEDICATION_ID", medicationId);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                (int) medicationId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create notification channel for Android 8.0+
        createNotificationChannel(context);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)  // You'll need to add this icon to your resources
                .setContentTitle("Medication Reminder")
                .setContentText("Time to take " + medicationName + " (" + dosage + ")")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify((int) medicationId, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}