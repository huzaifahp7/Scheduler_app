package com.example.scheduler_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {

    private FloatingActionButton add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        LinearLayout mondayBox = view.findViewById(R.id.mondayBox);
        mondayBox.setOnClickListener(v -> openFragment(new MondayFragment()));

        LinearLayout tuesdayBox = view.findViewById(R.id.tuesdayBox);
        tuesdayBox.setOnClickListener(v -> openFragment(new TuesdayFragment()));

        LinearLayout thursdayBox = view.findViewById(R.id.thursdayBox);
        thursdayBox.setOnClickListener(v -> openFragment(new ThursdayFragment()));

        LinearLayout wednesdayBox = view.findViewById(R.id.wednesdayBox);
        wednesdayBox.setOnClickListener(v -> openFragment(new WednesdayFragment()));


        LinearLayout fridayBox = view.findViewById(R.id.fridayBox);
        fridayBox.setOnClickListener(v -> openFragment(new FridayFragment()));


        add = view.findViewById(R.id.addCourse);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });
        return view;
    }

    // Similar methods for Wednesday, Thursday, and Friday

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Course())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();
    }
}

