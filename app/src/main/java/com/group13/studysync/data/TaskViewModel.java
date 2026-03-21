package com.group13.studysync.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * ViewModel for the TaskMate app.
 * Acts as a bridge between the UI (which the second team works on) and the Repository (Member 1).
 * It survives configuration changes like screen rotations.
 */
public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application) {
        super(application);
        // Link to our data source...
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    // The UI should call this to save a new Task
    public void insert(Task task) {
        repository.insert(task);
    }

    // To update this task...
    public void update(Task task) {
        repository.update(task);
    }

    // To remove a task...
    public void delete(Task task) {
        repository.delete(task);
    }

    /**
     * Returns the LiveData list that the UI will observe.
     * The UI will automatically update whenever the database changes
     */
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}