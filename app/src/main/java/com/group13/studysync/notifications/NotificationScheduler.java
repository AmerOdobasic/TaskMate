package com.group13.studysync.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.group13.studysync.data.Task;
import com.group13.studysync.receivers.TaskNotificationReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// This is the utility class that will be called to schedule or cancel notifications
// Usage:
//   NotificationScheduler.scheduleNotification(context, task);  // when a task is added/edited
//   NotificationScheduler.cancelNotification(context, task);    // when a task is deleted or completed
public class NotificationScheduler {

    // Must match the format Member 2 uses in the Add Task form's DatePickerDialog
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    // How many hours before the due date to send the reminder (default: 24 hours before)
    private static final long REMINDER_OFFSET_MS = 24 * 60 * 60 * 1000L;

    public static void scheduleNotification(Context context, Task task) {
        // Check if the user has notifications enabled in Settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        if (!notificationsEnabled) return;

        // Parse the due date string into a timestamp
        long triggerTimeMs = parseDueDate(task.getDueDate());
        if (triggerTimeMs == -1) return; // if it couldn't parse the date it skips

        // Fire the notification 24 hours before the due date
        long alarmTimeMs = triggerTimeMs - REMINDER_OFFSET_MS;

        // Don't schedule if the alarm time is already in the past
        if (alarmTimeMs <= System.currentTimeMillis()) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = buildPendingIntent(context, task);

        // setAndAllowWhileIdle ensures the alarm fires even when the device is in low-power mode
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMs, pendingIntent);
    }

    public static void cancelNotification(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = buildPendingIntent(context, task);
        alarmManager.cancel(pendingIntent);
    }

    // Builds the PendingIntent that points to TaskNotificationReceiver with task details attached
    private static PendingIntent buildPendingIntent(Context context, Task task) {
        Intent intent = new Intent(context, TaskNotificationReceiver.class);
        intent.putExtra(TaskNotificationReceiver.EXTRA_TASK_ID, task.getId());
        intent.putExtra(TaskNotificationReceiver.EXTRA_TASK_TITLE, task.getTitle());
        intent.putExtra(TaskNotificationReceiver.EXTRA_TASK_DUE_DATE, task.getDueDate());

        // Use task ID as the request code so each task gets its own unique PendingIntent
        return PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    // Converts the dueDate string to milliseconds since epoch
    private static long parseDueDate(String dueDateStr) {
        if (dueDateStr == null || dueDateStr.isEmpty()) return -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = sdf.parse(dueDateStr);
            return date != null ? date.getTime() : -1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}