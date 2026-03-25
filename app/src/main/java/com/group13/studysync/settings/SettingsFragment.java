package com.group13.studysync.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Root scroll view
        ScrollView scrollView = new ScrollView(requireContext());
        scrollView.setBackgroundColor(Color.BLACK);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        scrollView.addView(root);

        // ── NOTIFICATIONS SECTION ──
        TextView notifHeader = makeHeader("Notifications");
        root.addView(notifHeader);

        // Divider
        root.addView(makeDivider());

        // Row: label + switch
        LinearLayout notifRow = new LinearLayout(requireContext());
        notifRow.setOrientation(LinearLayout.HORIZONTAL);
        notifRow.setPadding(0, 32, 0, 32);

        LinearLayout notifText = new LinearLayout(requireContext());
        notifText.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        notifText.setLayoutParams(textParams);

        TextView notifTitle = makeTitle("Enable Notifications");
        TextView notifSummary = makeSummary("Receive reminders before task due dates");
        notifText.addView(notifTitle);
        notifText.addView(notifSummary);

        Switch notifSwitch = new Switch(requireContext());
        notifSwitch.setChecked(prefs.getBoolean("notifications_enabled", true));
        notifSwitch.setOnCheckedChangeListener((btn, isChecked) ->
                prefs.edit().putBoolean("notifications_enabled", isChecked).apply());

        notifRow.addView(notifText);
        notifRow.addView(notifSwitch);
        root.addView(notifRow);

        // ── ABOUT SECTION ──
        root.addView(makeDivider());
        TextView aboutHeader = makeHeader("About");
        root.addView(aboutHeader);
        root.addView(makeDivider());

        LinearLayout versionRow = new LinearLayout(requireContext());
        versionRow.setOrientation(LinearLayout.VERTICAL);
        versionRow.setPadding(0, 32, 0, 32);
        versionRow.addView(makeTitle("Version"));
        versionRow.addView(makeSummary("1.0.0"));
        root.addView(versionRow);

        return scrollView;
    }

    private TextView makeHeader(String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#E50000")); // app red for section headers
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