package com.group13.studysync.ui;

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
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the new RecyclerView
        RecyclerView recyclerUrgent = view.findViewById(R.id.recycler_urgent_tasks);
        recyclerUrgent.setLayoutManager(new LinearLayoutManager(getContext()));

        // Reuse existing TaskAdapter
        TaskAdapter adapter = new TaskAdapter();
        recyclerUrgent.setAdapter(adapter);

        // Feed it just 2 urgent items for the dashboard view
        List<String> urgentTasks = Arrays.asList(
                "Finish Mobile Dev Assignment",
                "Study for Big Data Exam"
        );
        adapter.setTasks(urgentTasks);

        return view;
    }
}