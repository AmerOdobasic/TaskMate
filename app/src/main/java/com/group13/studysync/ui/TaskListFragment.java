package com.group13.studysync.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color; // Added this import
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import java.util.ArrayList;

public class TaskListFragment extends Fragment {

    private TaskAdapter adapter;
    private ArrayList<TaskItem> currentTasks;

    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String title = result.getData().getStringExtra("NEW_TASK_TITLE");
                    String desc = result.getData().getStringExtra("NEW_TASK_DESC");
                    int color = result.getData().getIntExtra("NEW_TASK_COLOR", Color.GRAY);

                    String date = result.getData().getStringExtra("NEW_TASK_DATE");
                    currentTasks.add(0, new TaskItem(title, desc, color, date));
                    adapter.setTasks(currentTasks);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        if (currentTasks == null) {
            currentTasks = new ArrayList<>();
        }
        adapter.setTasks(currentTasks);

        View fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        return view;
    }
}