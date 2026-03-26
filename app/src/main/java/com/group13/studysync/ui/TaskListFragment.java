package com.group13.studysync.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {

    private TaskAdapter adapter;
    private List<Task> currentDatabaseTasks = new ArrayList<>();

    // Tracks which sort is active: "priority", "date", or "name"
    private String currentSort = "priority";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        TextView tvNoTasks = view.findViewById(R.id.tv_no_tasks);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_tasks);
        Button btnPriority = view.findViewById(R.id.btn_sort_priority);
        Button btnDate = view.findViewById(R.id.btn_sort_date);
        Button btnName = view.findViewById(R.id.btn_sort_name);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        adapter.setOnTaskCompleteListener(position -> {
            Task finishedTask = currentDatabaseTasks.get(position);
            finishedTask.setComplete(true);
            taskViewModel.update(finishedTask);
        });

        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            currentDatabaseTasks.clear();

            for (Task task : tasks) {
                if (!task.isComplete()) {
                    currentDatabaseTasks.add(task);
                }
            }

            renderSorted();

            // Based on how many tasks you have, shows a message
            if (currentDatabaseTasks.isEmpty()) {
                tvNoTasks.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvNoTasks.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Sort button listeners — highlight active button red, others grey
        btnPriority.setOnClickListener(v -> {
            currentSort = "priority";
            setActiveButton(btnPriority, btnDate, btnName);
            renderSorted();
        });

        btnDate.setOnClickListener(v -> {
            currentSort = "date";
            setActiveButton(btnDate, btnPriority, btnName);
            renderSorted();
        });

        btnName.setOnClickListener(v -> {
            currentSort = "name";
            setActiveButton(btnName, btnPriority, btnDate);
            renderSorted();
        });

        View fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Sorts currentDatabaseTasks and pushes the result to the adapter
    private void renderSorted() {
        List<Task> sorted = new ArrayList<>(currentDatabaseTasks);

        switch (currentSort) {
            case "priority":
                // High → Medium → Low
                Collections.sort(sorted, (a, b) -> priorityRank(a.getPriority()) - priorityRank(b.getPriority()));
                break;
            case "date":
                // Earliest due date first
                Collections.sort(sorted, (a, b) -> compareDates(a.getDueDate(), b.getDueDate()));
                break;
            case "name":
                // Alphabetical A → Z
                Collections.sort(sorted, (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                break;
        }

        List<TaskItem> taskItems = new ArrayList<>();
        for (Task task : sorted) {
            int color = TaskColorHelper.getColorFromPriority(task.getPriority());
            taskItems.add(new TaskItem(task.getTitle(), task.getDescription(), color, task.getDueDate()));
        }
        adapter.setTasks(taskItems);
    }

    // Maps priority string to sort rank: High=0, Medium=1, Low=2
    private int priorityRank(String priority) {
        if (priority == null) return 3;
        switch (priority) {
            case "High":   return 0;
            case "Medium": return 1;
            default:       return 2;
        }
    }

    // Compares two date strings for sorting (handles M/d/yyyy format)
    private int compareDates(String a, String b) {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy", Locale.US);
        try {
            Date dateA = sdf.parse(a);
            Date dateB = sdf.parse(b);
            if (dateA == null || dateB == null) return 0;
            return dateA.compareTo(dateB);
        } catch (ParseException e) {
            return 0;
        }
    }

    // Highlights the active sort button red and resets the others to grey
    private void setActiveButton(Button active, Button... inactive) {
        active.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#E50000")));
        for (Button btn : inactive) {
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#333333")));
        }
    }
}