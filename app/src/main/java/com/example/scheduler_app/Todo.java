package com.example.scheduler_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Todo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_check);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int id = item.getItemId();
            if (id == R.id.bottom_class){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;}
            else if (id == R.id.bottom_test){
                startActivity(new Intent(getApplicationContext(), Test.class));
                finish();
                return true;}
            else if(id == R.id.bottom_check){
                return true;}
            return false;
        });
    }
}