package com.group13.studysync;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group13.studysync.notifications.NotificationsHelper;
import com.group13.studysync.settings.SettingsActivity;
import com.group13.studysync.ui.CalendarFragment;
import com.group13.studysync.ui.HomeFragment;
import com.group13.studysync.ui.TaskListFragment;
import com.group13.studysync.ui.TimerFragment;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final String KEY_TAB_INDEX = "current_tab_index";

    // 0 = Home, 1 = Calendar, 2 = Tasks
    private int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Restore tab index after rotation
        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt(KEY_TAB_INDEX, 0);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int enterAnim = 0, exitAnim = 0;
            int newTabIndex = 0;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                newTabIndex = 0;
            } else if (itemId == R.id.nav_calendar) {
                selectedFragment = new CalendarFragment();
                newTabIndex = 1;
            } else if (itemId == R.id.nav_timer) {
                selectedFragment = new TimerFragment();
                newTabIndex = 2;
            } else if (itemId == R.id.nav_tasks) {
                selectedFragment = new TaskListFragment();
                newTabIndex = 3;
            } else if (itemId == R.id.nav_settings) {
                selectedFragment = new com.group13.studysync.settings.SettingsFragment();
                newTabIndex = 4;
            }

            if (newTabIndex > currentTabIndex) {
                enterAnim = R.anim.slide_in_right;
                exitAnim = R.anim.slide_out_left;
            } else if (newTabIndex < currentTabIndex) {
                enterAnim = R.anim.slide_in_left;
                exitAnim = R.anim.slide_out_right;
            }

            currentTabIndex = newTabIndex;

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(enterAnim, exitAnim)
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        NotificationsHelper.createNotificationChannel(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    // Save the current tab so rotation doesn't lose it
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TAB_INDEX, currentTabIndex);
    }

    // Inflate the settings gear icon in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle settings icon tap
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}