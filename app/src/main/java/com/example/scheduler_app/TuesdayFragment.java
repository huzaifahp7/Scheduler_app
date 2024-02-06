package com.example.scheduler_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TuesdayFragment extends Fragment {

    private MyDatabaseHelper dbHelper;
    private RecyclerView coursesRecyclerView;
    private CourseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        dayLabel.setText("Tuesday"); // Set the day label

        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new MyDatabaseHelper(getContext());
        loadCoursesForDay("Tuesday");
        return view;
    }

    private void loadCoursesForDay(String day) {
        List<CourseModel> courses = dbHelper.getCoursesForDay(day);
        int backgroundColor = R.drawable.rounded_corner_tuesday;
        adapter = new CourseAdapter(getContext(), courses, backgroundColor);
        coursesRecyclerView.setAdapter(adapter);
    }
}

