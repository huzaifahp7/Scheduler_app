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

public class MainActivity extends AppCompatActivity {
    private ImageButton examButton;

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
                        .commit();
            } else if(id == R.id.home_menu_item){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            } else if (id == R.id.bottom_test) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ExamFragment())
                        .commit();
            } else if (id == R.id.bottom_assign) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AssignmentFragment())
                        .commit();
            } else if (id == R.id.bottom_check) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ToDoFragment()) // Use ToDoFragment as a fragment, not an activity
                        .commit();
            }

            return true;
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}