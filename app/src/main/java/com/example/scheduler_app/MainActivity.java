package com.example.scheduler_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Navigation code

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_class) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CourseFragment()) // Replace ClassFragment with your actual class fragment
                        .addToBackStack(null)
                        .commit();
            } else if(id == R.id.home_menu_item){
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed(); // This will close the app as usual
        }
    }

}