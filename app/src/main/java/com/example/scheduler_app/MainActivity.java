package com.example.scheduler_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button
                // Perform your action here
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, continue with your logic
        } else {
            // Permission is not granted, so request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1);
        }

        PeriodicWorkRequest reminderRequest = new PeriodicWorkRequest.Builder(ReminderWorker.class, 10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(reminderRequest);

        // Navigation code

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_class) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CourseFragment()) // Replace ClassFragment with your actual class fragment
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.home_menu_item) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.bottom_test) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ExamFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.bottom_assign) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AssignmentFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.bottom_check) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ToDoFragment()) // Use ToDoFragment as a fragment, not an activity
                        .addToBackStack(null)
                        .commit();
            }

            return true;
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }


    @Override
    @Deprecated
    public void onBackPressed() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        int homeItemId = R.id.home_menu_item; // Make sure this ID matches the one in your bottom navigation

        Log.d("HI", "onBackPressed: Current selected item ID: " + bottomNavigationView.getSelectedItemId());
        Log.d("HELLO", "onBackPressed: Home item ID: " + homeItemId);
        Log.d("BYE", "onBackPressed: Back stack entry count: " + getSupportFragmentManager().getBackStackEntryCount());

        if (bottomNavigationView.getSelectedItemId() != homeItemId &&
                getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("YES", "onBackPressed: Popping back stack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.d("OKAY", "onBackPressed: Navigating to home or exiting");
            if (bottomNavigationView.getSelectedItemId() != homeItemId) {
                bottomNavigationView.setSelectedItemId(homeItemId);
            } else {
                super.onBackPressed();
            }
        }
    }
}
