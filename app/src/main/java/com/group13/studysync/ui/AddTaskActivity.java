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
        Button btnSave = findViewById(R.id.btn_save_task);

        // Initialize ViewModel for local database operations
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        final String[] selectedDate = {"No Date Set"};

        // Instantiate calendar and display DatePickerDialog for target deadline
        btnPickDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new android.app.DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate[0] = (month + 1) + "/" + day + "/" + year;
                btnPickDate.setText("DATE: " + selectedDate[0]);
            }, cal.get(java.util.Calendar.YEAR),
                    cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
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
                // Construct new Task entity using Amer's required parameters
                Task newTask = new Task(title, desc, selectedDate[0], priority, false);

                taskViewModel.insert(newTask);

                finish();
            }
        });
    }
}