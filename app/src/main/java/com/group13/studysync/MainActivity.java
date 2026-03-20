package com.group13.studysync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group13.studysync.ui.HomeFragment;
import com.group13.studysync.ui.TaskListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Start by showing the Home Dashboard
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        // Listen for clicks on the bottom bar
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Variables to hold our animation choices
            int enterAnim = 0;
            int exitAnim = 0;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                // Moving Left to Home
                enterAnim = R.anim.slide_in_left;
                exitAnim = R.anim.slide_out_right;

            } else if (itemId == R.id.nav_tasks) {
                selectedFragment = new TaskListFragment();
                // Moving Right to Tasks
                enterAnim = R.anim.slide_in_right;
                exitAnim = R.anim.slide_out_left;
            }

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