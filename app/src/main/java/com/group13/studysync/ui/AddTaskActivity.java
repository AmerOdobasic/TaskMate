package com.group13.studysync.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.group13.studysync.R;

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

        // Array used to store the date so we can edit it inside the popup
        final String[] selectedDate = {"No Date Set"};

        // Show the Calendar Popup when clicked
        btnPickDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new android.app.DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate[0] = (month + 1) + "/" + day + "/" + year;
                btnPickDate.setText("DATE: " + selectedDate[0]);
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString().trim();
            String desc = etTaskDesc.getText().toString().trim();

            // Colours separating task priorities
            int selectedColor = android.graphics.Color.parseColor("#444444"); // Low: Very Dark Grey

            if (rbHigh.isChecked()) {
                selectedColor = android.graphics.Color.parseColor("#E50000"); // High: Persona Red
            } else if (rbMed.isChecked()) {
                selectedColor = android.graphics.Color.parseColor("#AAAAAA"); // Normal: Bright Grey
            }

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            } else {
                android.content.Intent resultIntent = new android.content.Intent();
                resultIntent.putExtra("NEW_TASK_TITLE", title);
                resultIntent.putExtra("NEW_TASK_DESC", desc);
                resultIntent.putExtra("NEW_TASK_COLOR", selectedColor);
                resultIntent.putExtra("NEW_TASK_DATE", selectedDate[0]); // SEND THE DATE!

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}