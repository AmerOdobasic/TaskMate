package com.group13.studysync.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.group13.studysync.R;
import java.util.Locale;

public class TimerFragment extends Fragment {

    private TextView tvTimerDisplay;
    private Button btnTimerControl, btnTimerReset;
    private Button btn5m, btn25m, btn45m, btn60m;
    private TimerViewModel timerViewModel;

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

        timerViewModel = new ViewModelProvider(requireActivity()).get(TimerViewModel.class);

        // Restores correct button based on the ViewModel's memory
        restoreButtonState();

        timerViewModel.getTimerLiveData().observe(getViewLifecycleOwner(), this::updateCountDownText);

        timerViewModel.getTimerRunningLiveData().observe(getViewLifecycleOwner(), isRunning -> {
            if (isRunning) {
                btnTimerControl.setText("PAUSE");
            } else {
                long currentMillis = timerViewModel.getTimerLiveData().getValue() != null ? timerViewModel.getTimerLiveData().getValue() : 0;
                long startMillis = timerViewModel.getStartingTimeInMillis();

                if (currentMillis > 0 && currentMillis < startMillis) {
                    btnTimerControl.setText("RESUME");
                } else {
                    btnTimerControl.setText("START");
                }
            }
        });

        timerViewModel.getTimerFinishedEvent().observe(getViewLifecycleOwner(), isFinished -> {
            if (isFinished) {
                playAlarmAndShowPopup();
                timerViewModel.resetFinishedEvent();
            }
        });

        btnTimerControl.setOnClickListener(v -> {
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()).start();
            Boolean isRunning = timerViewModel.getTimerRunningLiveData().getValue();
            if (isRunning != null && isRunning) {
                timerViewModel.pauseTimer();
            } else {
                timerViewModel.startTimer();
            }
        });

        btnTimerReset.setOnClickListener(v -> {
            timerViewModel.resetTimer();
            btnTimerControl.setText("START");
        });

        btn5m.setOnClickListener(v -> setTimerDuration(300000, btn5m));
        btn25m.setOnClickListener(v -> setTimerDuration(1500000, btn25m));
        btn45m.setOnClickListener(v -> setTimerDuration(2700000, btn45m));
        btn60m.setOnClickListener(v -> setTimerDuration(3600000, btn60m));

        return view;
    }

    private void restoreButtonState() {
        resetButtonColors();
        long currentStart = timerViewModel.getStartingTimeInMillis();
        if (currentStart == 300000) {
            btn5m.setBackgroundColor(Color.parseColor("#E50000"));
            btn5m.setTextColor(Color.WHITE);
        } else if (currentStart == 1500000) {
            btn25m.setBackgroundColor(Color.parseColor("#E50000"));
            btn25m.setTextColor(Color.WHITE);
        } else if (currentStart == 2700000) {
            btn45m.setBackgroundColor(Color.parseColor("#E50000"));
            btn45m.setTextColor(Color.WHITE);
        } else if (currentStart == 3600000) {
            btn60m.setBackgroundColor(Color.parseColor("#E50000"));
            btn60m.setTextColor(Color.WHITE);
        }
    }

    private void setTimerDuration(long millis, Button selectedButton) {
        // Block the buttons from resetting the clock if it is actively running
        Boolean isRunning = timerViewModel.getTimerRunningLiveData().getValue();
        if (isRunning != null && isRunning) {
            return;
        }

        timerViewModel.setTimerDuration(millis);
        resetButtonColors();
        selectedButton.setBackgroundColor(Color.parseColor("#E50000"));
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

    private void updateCountDownText(long timeLeftInMillis) {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        if (tvTimerDisplay != null) {
            tvTimerDisplay.setText(timeFormatted);
        }
    }

    private void playAlarmAndShowPopup() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (notification == null) {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            Ringtone r = RingtoneManager.getRingtone(requireContext(), notification);
            r.play();

            new AlertDialog.Builder(requireContext())
                    .setTitle("Time's Up!")
                    .setMessage("Your focus session is complete. Take a break!")
                    .setPositiveButton("OK", (dialog, which) -> r.stop())
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}