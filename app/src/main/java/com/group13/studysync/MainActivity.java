package com.group13.studysync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group13.studysync.ui.HomeFragment;
import com.group13.studysync.ui.CalendarFragment;
import com.group13.studysync.ui.TimerFragment;
import com.group13.studysync.ui.TaskListFragment;

public class MainActivity extends AppCompatActivity {

    // 0 = Home, 1 = Calendar, 2 = Tasks
    private int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            }

            // Determine which way we move
            if (newTabIndex > currentTabIndex) {
                // Moving Right (e.g., Home to Calendar)
                enterAnim = R.anim.slide_in_right;
                exitAnim = R.anim.slide_out_left;
            } else if (newTabIndex < currentTabIndex) {
                // Moving Left (e.g., Tasks to Calendar)
                enterAnim = R.anim.slide_in_left;
                exitAnim = R.anim.slide_out_right;
            }

            currentTabIndex = newTabIndex; // Update our tracker

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(enterAnim, exitAnim)
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}