package com.group13.studysync.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.group13.studysync.notifications.NotificationScheduler;

import java.util.List;

/**
 * This repo class will manage data operations for Tasks.
 * This provides a clean API to the rest of the app for data access.
 */
public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private Application application;

    public TaskRepository(Application application) {
        this.application = application;
        // Initialize the database and get the DAO
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        // Room handles LiveData on a background thread automatically
        allTasks = taskDao.getAllTasks();
    }

    // This will insert a new task into the database
    // by using a background thread to avoid blocking the UI
    public void insert(Task task) {
        new Thread(() -> {
            taskDao.insert(task);
            // Schedule a notification reminder for this task after saving
            NotificationScheduler.scheduleNotification(application, task);
        }).start();
    }

    // Updates an existing task (like marking as complete).
    public void update(Task task) {
        new Thread(() -> {
            taskDao.update(task);
            // If task is completed, cancel its notification
            if (task.isComplete()) {
                NotificationScheduler.cancelNotification(application, task);
            }
        }).start();
    }

    // Deletes a specific task from the database
    public void delete(Task task) {
        new Thread(() -> {
            taskDao.delete(task);
            // Cancel the notification when a task is deleted
            NotificationScheduler.cancelNotification(application, task);
        }).start();
    }

    /**
     * Returns an observable list of all tasks.
     * Such that the UI will automatically update whenever the data changes.
     */
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}