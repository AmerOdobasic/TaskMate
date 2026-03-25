package com.group13.studysync.ui;

import android.os.CountDownTimer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long startingTimeInMillis = 1500000; // Defaults to 25 mins
    private long timeLeftInMillis = startingTimeInMillis;

    // LiveData so the Fragment can "listen" to changes even after reopening
    private final MutableLiveData<Long> timerLiveData = new MutableLiveData<>(timeLeftInMillis);
    private final MutableLiveData<Boolean> timerRunningLiveData = new MutableLiveData<>(isTimerRunning);
    private final MutableLiveData<Boolean> timerFinishedEvent = new MutableLiveData<>(false);

    public LiveData<Long> getTimerLiveData() { return timerLiveData; }
    public LiveData<Boolean> getTimerRunningLiveData() { return timerRunningLiveData; }
    public LiveData<Boolean> getTimerFinishedEvent() { return timerFinishedEvent; }

    public long getStartingTimeInMillis() { return startingTimeInMillis; }

    public void setTimerDuration(long millis) {
        if (countDownTimer != null) countDownTimer.cancel();
        startingTimeInMillis = millis;
        timeLeftInMillis = startingTimeInMillis;
        isTimerRunning = false;
        timerRunningLiveData.setValue(isTimerRunning);
        timerLiveData.setValue(timeLeftInMillis);
    }

    public void startTimer() {
        if (isTimerRunning) return;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                timerLiveData.setValue(timeLeftInMillis);
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timeLeftInMillis = startingTimeInMillis;
                timerRunningLiveData.setValue(isTimerRunning);
                timerLiveData.setValue(timeLeftInMillis);
                timerFinishedEvent.setValue(true); // Triggers the alarm in the UI
            }
        }.start();

        isTimerRunning = true;
        timerRunningLiveData.setValue(isTimerRunning);
    }

    public void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false;
        timerRunningLiveData.setValue(isTimerRunning);
    }

    public void resetTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false;
        timeLeftInMillis = startingTimeInMillis;
        timerRunningLiveData.setValue(isTimerRunning);
        timerLiveData.setValue(timeLeftInMillis);
    }

    public void resetFinishedEvent() {
        timerFinishedEvent.setValue(false);
    }
}