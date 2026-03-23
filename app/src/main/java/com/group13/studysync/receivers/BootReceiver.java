package com.group13.studysync.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.group13.studysync.data.AppDatabase;
import com.group13.studysync.data.Task;
import com.group13.studysync.notifications.NotificationScheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// When the phone reboots, ALL scheduled alarms are wiped by Android.
// This receiver listens for the BOOT_COMPLETED system event and
// reschedules every incomplete task's notification automatically.
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) return;

        // Room database queries can't run on the main thread — use a background thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Get all tasks from the database
            AppDatabase db = AppDatabase.getInstance(context);
            List<Task> allTasks = db.taskDao().getAllTasksSync();

            // Reschedule a notification for every task that isn't done yet
            for (Task task : allTasks) {
                if (!task.isComplete()) {
                    NotificationScheduler.scheduleNotification(context, task);
                }
            }
        });
    }
}
