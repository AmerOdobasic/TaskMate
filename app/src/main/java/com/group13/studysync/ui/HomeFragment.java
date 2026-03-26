package com.group13.studysync.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TaskAdapter adapter;
    private List<Task> displayedUrgentTasks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvDueToday = view.findViewById(R.id.tv_due_count);
        TextView tvUpcoming = view.findViewById(R.id.tv_completed_count);
        RecyclerView recyclerUrgent = view.findViewById(R.id.recycler_urgent_tasks);

        recyclerUrgent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter();
        recyclerUrgent.setAdapter(adapter);

        TaskViewModel taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        adapter.setOnTaskCompleteListener(position -> {
            Task finishedTask = displayedUrgentTasks.get(position);
            finishedTask.setComplete(true);
            taskViewModel.update(finishedTask);
        });

        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            int todayCount = 0;
            int upcomingCount = 0;

            List<TaskItem> urgentUIItems = new ArrayList<>();
            displayedUrgentTasks.clear();

            // Get exact string for Today in yyyy-MM-dd format to match AddTaskActivity
            Calendar cal = Calendar.getInstance();
            String todayStr = String.format(Locale.US, "%04d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            // Strict boundaries for "Tomorrow" and "7 Days From Now"
            Calendar midnightCal = Calendar.getInstance();
            midnightCal.set(Calendar.HOUR_OF_DAY, 23);
            midnightCal.set(Calendar.MINUTE, 59);
            midnightCal.set(Calendar.SECOND, 59);
            long endOfToday = midnightCal.getTimeInMillis();
            long sevenDaysFromNow = endOfToday + (7L * 24 * 60 * 60 * 1000);

            // Updated to parse yyyy-MM-dd format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            for (Task task : tasks) {
                if (!task.isComplete()) {

                    // Extract just the date part in case time is appended
                    String datePart = task.getDueDate().split(" ")[0];

                    // If task due exactly today — use startsWith to handle date+time format
                    if  (task.getDueDate().startsWith(todayStr) && "High".equals(task.getPriority())) {
                        todayCount++;
                        int color = TaskColorHelper.getColorFromPriority(task.getPriority());
                        urgentUIItems.add(new TaskItem(task.getTitle(), task.getDescription(), color, task.getDueDate()));
                        displayedUrgentTasks.add(task);
                    }
                    // If task is due any other day
                    else {
                        try {
                            Date taskDate = sdf.parse(datePart);
                            // Ensure the date is strictly after today + within the next 7 days
                            if (taskDate != null && taskDate.getTime() > endOfToday && taskDate.getTime() <= sevenDaysFromNow) {
                                upcomingCount++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            tvDueToday.setText(String.valueOf(todayCount));
            tvUpcoming.setText(String.valueOf(upcomingCount));
            adapter.setTasks(urgentUIItems);
        });

        return view;
    }
}