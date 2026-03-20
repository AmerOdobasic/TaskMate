package com.group13.studysync.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerUrgent = view.findViewById(R.id.recycler_urgent_tasks);
        recyclerUrgent.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskAdapter adapter = new TaskAdapter();
        recyclerUrgent.setAdapter(adapter);

        // Creating TaskItem objects
        List<TaskItem> urgentTasks = new ArrayList<>();
        adapter.setTasks(urgentTasks);

        return view;
    }
}