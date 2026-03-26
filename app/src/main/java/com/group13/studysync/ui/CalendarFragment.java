package com.group13.studysync.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import com.group13.studysync.data.Task;
import com.group13.studysync.data.TaskViewModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private String selectedDate = "";
    private List<Task> allDatabaseTasks = new ArrayList<>();
    private List<Task> displayedDatabaseTasks = new ArrayList<>();
    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TextView tvScheduleHeader = view.findViewById(R.id.tv_schedule_header);
        RecyclerView recyclerCalendar = view.findViewById(R.id.recycler_calendar_tasks);

        recyclerCalendar.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter();
        recyclerCalendar.setAdapter(adapter);

        // Initialize ViewModel
        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Set the listener
        adapter.setOnTaskCompleteListener(position -> {
            Task finishedTask = displayedDatabaseTasks.get(position);
            finishedTask.setComplete(true);
            taskViewModel.update(finishedTask);
        });

        // Observe the data
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            allDatabaseTasks = tasks;
            filterTasksByDate();
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Format as yyyy-MM-dd to match the date format saved by AddTaskActivity
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            tvScheduleHeader.setText("Tasks for " + selectedDate);
            filterTasksByDate();
        });

        // Auto-select today's date on load so tasks show immediately
        Calendar cal = Calendar.getInstance();
        selectedDate = String.format("%04d-%02d-%02d",
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        tvScheduleHeader.setText("Tasks for " + selectedDate);
        filterTasksByDate();

        return view;
    }

    private void filterTasksByDate() {
        if (selectedDate.isEmpty() || allDatabaseTasks == null) return;

        List<TaskItem> dailyTasks = new ArrayList<>();
        displayedDatabaseTasks.clear();

        for (Task task : allDatabaseTasks) {
            // Use startsWith to handle tasks with time appended e.g. "2026-03-25 21:17"
            if (task.getDueDate().startsWith(selectedDate) && !task.isComplete()) {
                int color = TaskColorHelper.getColorFromPriority(task.getPriority());
                dailyTasks.add(new TaskItem(task.getTitle(), task.getDescription(), color, task.getDueDate()));
                displayedDatabaseTasks.add(task);
            }
        }
        adapter.setTasks(dailyTasks);
    }
}