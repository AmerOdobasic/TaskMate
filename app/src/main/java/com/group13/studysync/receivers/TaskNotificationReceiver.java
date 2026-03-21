package com.group13.studysync.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.group13.studysync.notifications.NotificationsHelper;

// This BroadcastReceiver is triggered by AlarmManager at the scheduled time
// It receives the task info and tells NotificationsHelper to show the notification
public class TaskNotificationReceiver extends BroadcastReceiver {

    // These keys must match what NotificationScheduler puts into the Intent extras
    public static final String EXTRA_TASK_ID = "extra_task_id";
    public static final String EXTRA_TASK_TITLE = "extra_task_title";
    public static final String EXTRA_TASK_DUE_DATE = "extra_task_due_date";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Pull the task details out of the Intent extras
        int taskId = intent.getIntExtra(EXTRA_TASK_ID, -1);
        String taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE);
        String dueDate = intent.getStringExtra(EXTRA_TASK_DUE_DATE);

        // Safety check : don't show a broken notification
        if (taskId == -1 || taskTitle == null) return;

        // Show the notification
        NotificationsHelper.showTaskNotification(context, taskId, taskTitle, dueDate);
    }
}