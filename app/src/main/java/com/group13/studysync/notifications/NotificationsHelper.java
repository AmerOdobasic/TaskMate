package com.group13.studysync.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

// This class handles:
// Creating the notification channel (required for Android 8.0+)
// Building and displaying notifications
public class NotificationsHelper {

    public static final String CHANNEL_ID = "deadline_reminders";
    public static final String CHANNEL_NAME = "Deadline Reminders";
    public static final String CHANNEL_DESC = "Notifications for upcoming task due dates";

    // Call this once when the app starts (in MainActivity or Application class)
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // Builds and shows the notification for a specific task
    public static void showTaskNotification(Context context, int taskId, String taskTitle, String dueDate) {

        // Intent to open the app when the user taps the notification
        // Once Member 4 builds TaskDetailActivity, you can replace this with that Activity
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // swap with your own icon later
                .setContentTitle("Task Due Soon: " + taskTitle)
                .setContentText("Due: " + dueDate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // dismisses notification when tapped

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(taskId, builder.build());
        }
    }
}