package com.example.myapplication.Medication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;

public class MedicationReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification for the reminder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "medication_reminder_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Medication Reminder")
                .setContentText("It's time to take your medication!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}

