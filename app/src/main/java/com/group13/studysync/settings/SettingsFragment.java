package com.group13.studysync.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        ScrollView scrollView = new ScrollView(requireContext());
        scrollView.setBackgroundColor(Color.BLACK);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        scrollView.addView(root);

        // ── NOTIFICATIONS SECTION ──
        root.addView(makeHeader("Notifications"));
        root.addView(makeDivider());

        LinearLayout notifRow = makeRow();

        LinearLayout notifText = new LinearLayout(requireContext());
        notifText.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        notifText.setLayoutParams(textParams);
        notifText.addView(makeTitle("Enable Notifications"));
        notifText.addView(makeSummary("Receive reminders before task due dates"));

        boolean notifEnabled = prefs.getBoolean("notifications_enabled", true);
        TextView notifStateLabel = makeStateLabel(notifEnabled ? "ON" : "OFF", notifEnabled);

        SwitchCompat notifSwitch = makeSwitch(notifEnabled);
        notifSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
            notifStateLabel.setText(isChecked ? "ON" : "OFF");
            notifStateLabel.setTextColor(isChecked ? Color.parseColor("#E50000") : Color.parseColor("#888888"));
        });

        notifRow.addView(notifText);
        notifRow.addView(notifStateLabel);
        notifRow.addView(notifSwitch);
        root.addView(notifRow);

        // ── ABOUT SECTION ──
        root.addView(makeDivider());
        root.addView(makeHeader("About"));
        root.addView(makeDivider());

        LinearLayout versionRow = new LinearLayout(requireContext());
        versionRow.setOrientation(LinearLayout.VERTICAL);
        versionRow.setPadding(0, 32, 0, 32);
        versionRow.addView(makeTitle("Version"));
        versionRow.addView(makeSummary("1.0.0"));
        root.addView(versionRow);

        return scrollView;
    }

    private LinearLayout makeRow() {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 32, 0, 32);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        return row;
    }

    private TextView makeStateLabel(String text, boolean isOn) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(isOn ? Color.parseColor("#E50000") : Color.parseColor("#888888"));
        tv.setTextSize(13f);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(12);
        tv.setLayoutParams(params);
        return tv;
    }

    private SwitchCompat makeSwitch(boolean checked) {
        SwitchCompat sw = new SwitchCompat(requireContext());
        sw.setChecked(checked);
        sw.setThumbTintList(android.content.res.ColorStateList.valueOf(
                checked ? Color.parseColor("#E50000") : Color.parseColor("#555555")));
        sw.setTrackTintList(android.content.res.ColorStateList.valueOf(
                checked ? Color.parseColor("#7A0000") : Color.parseColor("#333333")));
        sw.setOnCheckedChangeListener((btn, isChecked) -> {
            sw.setThumbTintList(android.content.res.ColorStateList.valueOf(
                    isChecked ? Color.parseColor("#E50000") : Color.parseColor("#555555")));
            sw.setTrackTintList(android.content.res.ColorStateList.valueOf(
                    isChecked ? Color.parseColor("#7A0000") : Color.parseColor("#333333")));
        });
        return sw;
    }

    private TextView makeHeader(String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#E50000"));
        tv.setTextSize(13f);
        tv.setPadding(0, 32, 0, 8);
        return tv;
    }

    private TextView makeTitle(String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(16f);
        return tv;
    }

    private TextView makeSummary(String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#AAAAAA"));
        tv.setTextSize(13f);
        tv.setPadding(0, 4, 0, 0);
        return tv;
    }

    private View makeDivider() {
        View divider = new View(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(0, 8, 0, 8);
        divider.setLayoutParams(params);
        divider.setBackgroundColor(Color.parseColor("#333333"));
        return divider;
    }
}