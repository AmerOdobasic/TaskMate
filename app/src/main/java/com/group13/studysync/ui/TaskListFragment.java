package com.group13.studysync.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import com.group13.studysync.data.TaskViewModel;
import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {

    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel scoped to the Activity to persist across lifecycle changes
        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity())
                .get(TaskViewModel.class);

        // Observe Room database changes via LiveData for reactive UI updates
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            List<TaskItem> taskItems = new ArrayList<>();
            for (com.group13.studysync.data.Task task : tasks) {

                // Map database priority strings to UI color integers
                int color = TaskColorHelper.getColorFromPriority(task.getPriority());

                taskItems.add(new TaskItem(
                        task.getTitle(),
                        task.getDescription(),
                        color,
                        task.getDueDate()
                ));
            }
            adapter.setTasks(taskItems);
        });

        // Launch AddTaskActivity. State management is handled natively by Room/LiveData
        View fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
        });

        return view;
    }
}