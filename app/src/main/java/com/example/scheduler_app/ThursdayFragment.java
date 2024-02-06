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

public class ThursdayFragment extends Fragment implements CourseUpdate{

    private MyDatabaseHelper dbHelper;
    private RecyclerView coursesRecyclerView;
    private CourseAdapter adapter;
    private CourseUpdate listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        dayLabel.setText("Thursday"); // Set the day label

        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new MyDatabaseHelper(getContext());
        loadCoursesForDay("Thursday");
        return view;
    }

    private void loadCoursesForDay(String day) {
        List<CourseModel> courses = dbHelper.getCoursesForDay(day);
        int backgroundColor = R.drawable.rounded_corner_thursday;
        adapter = new CourseAdapter(getContext(), courses, backgroundColor, dbHelper, listener);
        coursesRecyclerView.setAdapter(adapter);
    }
    private void refreshCourseList() {
        List<CourseModel> courses = dbHelper.getCoursesForDay("Thursday");
        adapter = new CourseAdapter(getContext(), courses, R.color.thursdayColor, dbHelper, listener);
        coursesRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onCourseUpdated() {
        loadCoursesForDay("Thurday");
    }

}

