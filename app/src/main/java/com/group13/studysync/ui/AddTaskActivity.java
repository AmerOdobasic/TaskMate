package com.group13.studysync.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.group13.studysync.R;
import com.group13.studysync.data.Task;
import com.group13.studysync.data.TaskViewModel;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText etTaskTitle = findViewById(R.id.et_task_title);
        EditText etTaskDesc = findViewById(R.id.et_task_desc);
        RadioButton rbHigh = findViewById(R.id.rb_high);
        RadioButton rbMed = findViewById(R.id.rb_med);
        Button btnPickDate = findViewById(R.id.btn_pick_date);
        Button btnPickTime = findViewById(R.id.btn_pick_time);
        Button btnSave = findViewById(R.id.btn_save_task);

        // Initialize ViewModel for local database operations
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        final String[] selectedDate = {"No Date Set"};
        final String[] selectedTime = {"No Time Set"};

        // Instantiate calendar and display DatePickerDialog for target deadline
        btnPickDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new android.app.DatePickerDialog(this, (view, year, month, day) -> {
                // Format as yyyy-MM-dd to match NotificationScheduler's DATE_FORMAT
                selectedDate[0] = String.format("%04d-%02d-%02d", year, month + 1, day);
                btnPickDate.setText("DATE: " + selectedDate[0]);
            }, cal.get(java.util.Calendar.YEAR),
                    cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });

        // TimePickerDialog in 12-hour AM/PM mode
        btnPickTime.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new android.app.TimePickerDialog(this, (view, hour, minute) -> {
                // Convert 24hr to 12hr AM/PM for display
                String amPm = hour < 12 ? "AM" : "PM";
                int hour12 = hour % 12;
                if (hour12 == 0) hour12 = 12;
                // Store in 12hr format for display but save as 24hr for NotificationScheduler
                selectedTime[0] = String.format("%02d:%02d", hour, minute); // 24hr for scheduler
                String displayTime = String.format("%d:%02d %s", hour12, minute, amPm); // 12hr for button
                btnPickTime.setText("TIME: " + displayTime);
            }, cal.get(java.util.Calendar.HOUR_OF_DAY),
                    cal.get(java.util.Calendar.MINUTE), false).show(); // false = 12hr AM/PM picker
        });

        btnSave.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString().trim();
            String desc = etTaskDesc.getText().toString().trim();

            // Map radio button selection to database priority schema
            String priority = "Low";
            if (rbHigh.isChecked()) {
                priority = "High";
            } else if (rbMed.isChecked()) {
                priority = "Medium";
            }

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            } else {
                // Combine date + time into one string e.g. "2026-03-25 14:30"
                String dueDateTime = selectedDate[0];
                if (!selectedTime[0].equals("No Time Set")) {
                    dueDateTime += " " + selectedTime[0];
                }

                // Construct new Task entity using Amer's required parameters
                Task newTask = new Task(title, desc, dueDateTime, priority, false);

                taskViewModel.insert(newTask);

                finish();
            }
        });
    }
}