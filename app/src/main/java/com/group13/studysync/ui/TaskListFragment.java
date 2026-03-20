package com.group13.studysync.ui;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Arrays;
import java.util.List;

public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ArrayList<String> currentTasks;

    // Waits for AddTaskActivity to finish, unboxes the text, and updates the list.
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // Extract the text from the envelope
                    String newTask = result.getData().getStringExtra("NEW_TASK_TITLE");

                    // Add it to the top of our list (index 0)
                    currentTasks.add(0, newTask);

                    // Tells the Adapter to refresh the screen with the new data
                    adapter.setTasks(currentTasks);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        // AMER, WHEN THE DATABASE IS DONE, ALL TEST DATA WILL BE DELETED
        if (currentTasks == null) {
            currentTasks = new ArrayList<>(Arrays.asList(
                    "Finish Mobile Dev Assignment",
                    "Grocery Shopping",
                    "Reply to Emails"
            ));
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