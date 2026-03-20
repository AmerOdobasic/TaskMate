package com.group13.studysync.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.group13.studysync.R;

public class AddTaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText etTaskTitle = findViewById(R.id.et_task_title);
        Button btnSave = findViewById(R.id.btn_save_task);

        btnSave.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
            } else {
                // Create an empty envelope (Intent)
                android.content.Intent resultIntent = new android.content.Intent();
                // Put the typed text inside the envelope and label it "NEW_TASK_TITLE"
                resultIntent.putExtra("NEW_TASK_TITLE", title);
                // Tells android this was a success, and hands over the envelope
                setResult(RESULT_OK, resultIntent);

                Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close the screen
            }
        });
    }
}