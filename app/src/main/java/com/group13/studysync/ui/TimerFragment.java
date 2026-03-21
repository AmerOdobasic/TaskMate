package com.group13.studysync.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.group13.studysync.R;
import java.util.Locale;

public class TimerFragment extends Fragment {

    private TextView tvTimerDisplay;
    private Button btnTimerControl, btnTimerReset;
    private Button btn5m, btn25m, btn45m, btn60m;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    // track the specific "starting" time so the reset button knows what to go back to
    private long startingTimeInMillis = 1500000; // Defaults to 25 mins
    private long timeLeftInMillis = startingTimeInMillis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        tvTimerDisplay = view.findViewById(R.id.tv_timer_display);
        btnTimerControl = view.findViewById(R.id.btn_timer_control);
        btnTimerReset = view.findViewById(R.id.btn_timer_reset);

        btn5m = view.findViewById(R.id.btn_5m);
        btn25m = view.findViewById(R.id.btn_25m);
        btn45m = view.findViewById(R.id.btn_45m);
        btn60m = view.findViewById(R.id.btn_60m);

        btnTimerControl.setOnClickListener(v -> {
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()).start();
            if (isTimerRunning) pauseTimer();
            else startTimer();
        });

        btnTimerReset.setOnClickListener(v -> resetTimer());

        // Listen for preset clicks, convert mins to milliseconds
        btn5m.setOnClickListener(v -> setTimerDuration(300000, btn5m));
        btn25m.setOnClickListener(v -> setTimerDuration(1500000, btn25m));
        btn45m.setOnClickListener(v -> setTimerDuration(2700000, btn45m));
        btn60m.setOnClickListener(v -> setTimerDuration(3600000, btn60m));

        updateCountDownText();
        return view;
    }

    // Handles swapping the time and turning the clicked button red
    private void setTimerDuration(long millis, Button selectedButton) {
        if (countDownTimer != null) countDownTimer.cancel();

        startingTimeInMillis = millis;
        timeLeftInMillis = startingTimeInMillis;
        isTimerRunning = false;

        updateCountDownText();
        btnTimerControl.setText("START");

        resetButtonColors();
        selectedButton.setBackgroundColor(Color.parseColor("#E50000")); // Persona Red
        selectedButton.setTextColor(Color.WHITE);
    }

    private void resetButtonColors() {
        int darkGrey = Color.parseColor("#212121");
        int lightGrey = Color.parseColor("#AAAAAA");

        btn5m.setBackgroundColor(darkGrey); btn5m.setTextColor(lightGrey);
        btn25m.setBackgroundColor(darkGrey); btn25m.setTextColor(lightGrey);
        btn45m.setBackgroundColor(darkGrey); btn45m.setTextColor(lightGrey);
        btn60m.setBackgroundColor(darkGrey); btn60m.setTextColor(lightGrey);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                btnTimerControl.setText("START");
                timeLeftInMillis = startingTimeInMillis;
                updateCountDownText();
            }
        }.start();

        isTimerRunning = true;
        btnTimerControl.setText("PAUSE");
    }

    private void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false;
        btnTimerControl.setText("RESUME");
    }

    private void resetTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false;
        timeLeftInMillis = startingTimeInMillis;
        updateCountDownText();
        btnTimerControl.setText("START");
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        if (tvTimerDisplay != null) {
            tvTimerDisplay.setText(timeFormatted);
        }
    }
}