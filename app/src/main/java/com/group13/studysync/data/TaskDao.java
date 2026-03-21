package com.group13.studysync.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// This will handle all of our database queries.
@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task_table ORDER BY dueDate ASC")
    LiveData<List<Task>> getAllTasks();

    // Added synchronous version of getAllTasks() needed by BootReceiver
    // LiveData cannot be used in a BroadcastReceiver since it requires a lifecycle owner
    @Query("SELECT * FROM task_table")
    List<Task> getAllTasksSync();
}