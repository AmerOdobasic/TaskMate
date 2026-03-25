package com.group13.studysync.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

    // Must match the format used in the Add Task form's DatePickerDialog
    private static final String DATE_FORMAT = "M/d/yyyy";

    // How long before the due date to fire the reminder — currently set to 24 hours
    private static final long REMINDER_OFFSET_MS = 24 * 60 * 60 * 1000L;

    public static void scheduleNotification(Context context, Task task) {
        // Convert the due date string into a millisecond timestamp
        long triggerTimeMs = parseDueDate(task.getDueDate());
        if (triggerTimeMs == -1) return; // skip if the date couldn't be parsed

        // Subtract 24 hours to get the reminder time
        long alarmTimeMs = triggerTimeMs - REMINDER_OFFSET_MS;

        // Don't schedule if the reminder time is already in the past
        if (alarmTimeMs <= System.currentTimeMillis()) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = buildPendingIntent(context, task);

        // On Android 12+ check if we have permission to schedule exact alarms
        // If not, fall back to setAndAllowWhileIdle which doesn't require special permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMs, pendingIntent);
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMs, pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMs, pendingIntent);
        }
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

    // Converts the dueDate string (e.g. "2026-03-25") to milliseconds since epoch
    // Returns -1 if the string is null, empty, or cannot be parsed
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