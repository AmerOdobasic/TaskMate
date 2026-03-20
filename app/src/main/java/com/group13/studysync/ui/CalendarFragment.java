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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TextView tvScheduleHeader = view.findViewById(R.id.tv_schedule_header);
        RecyclerView recyclerCalendar = view.findViewById(R.id.recycler_calendar_tasks);

        recyclerCalendar.setLayoutManager(new LinearLayoutManager(getContext()));
        TaskAdapter adapter = new TaskAdapter();
        recyclerCalendar.setAdapter(adapter);

        // When the user clicks a date on the calendar:
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
            tvScheduleHeader.setText("Tasks for " + selectedDate);

            // The list is now completely empty.
            // Amer will replace this exact spot with: database.getTasksByDate(selectedDate);
            List<TaskItem> emptyList = new ArrayList<>();
            adapter.setTasks(emptyList);
        });

        return view;
    }
}