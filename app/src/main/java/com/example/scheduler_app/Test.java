package com.example.scheduler_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_test);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int id = item.getItemId();
                if (id == R.id.bottom_class){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                return true;}
                else if (id == R.id.bottom_test){
                    return true;}
                else if(id == R.id.bottom_check){
                    startActivity(new Intent(getApplicationContext(), Todo.class));
                    finish();
                    return true;}
            return false;
        });
    }
}