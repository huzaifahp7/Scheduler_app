package com.example.scheduler_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.scheduler_app.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ImageButton examButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //examButton = findViewById(R.id.examButton);
        //ExamModel initialExamModel = new ExamModel("Sample Date", "Sample Time", "Sample Location");
        // Set initial fragment on app launch
        //showFragment(ExamModelFragment.newInstance(initialExamModel));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_class);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int id = item.getItemId();
            if (id == R.id.bottom_class){
                return true;}
            else if (id == R.id.bottom_test){
                    startActivity(new Intent(getApplicationContext(), Test.class));
                    finish();
            return true;}
            else if(id == R.id.bottom_check){
                startActivity(new Intent(getApplicationContext(), Todo.class));
                finish();
                return true;}
            return false;
        });

        // Set click listeners for buttons
//        examButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ExamModel newExamModel = new ExamModel("New Date", "New Time", "New Location");
//                showFragment(ExamModelFragment.newInstance(newExamModel));
//            }
//        });


        // Add more click listeners for additional buttons/icons as needed
    //}

   /// private void showFragment(Fragment fragment) {
      //  FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        //fragmentTransaction.addToBackStack(null);  // Optional: Add to back stack for navigation
        //fragmentTransaction.commit();
    }
}