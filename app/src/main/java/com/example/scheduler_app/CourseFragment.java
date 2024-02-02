package com.example.scheduler_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        ListView listViewCourses = view.findViewById(R.id.listViewCourses);
        List<CourseModel> courses = getCourses();
        add = view.findViewById(R.id.addCourse);

        CourseAdapter adapter = new CourseAdapter(getContext(), courses);
        listViewCourses.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });

        return view;
    }

    private List<CourseModel> getCourses() {
        // This should be replaced with actual data retrieval logic
        List<CourseModel> courses = new ArrayList<>();
        courses.add(new CourseModel("Computer Science", "Dr. Smith", "10:00 AM", "Room 101"));
        courses.add(new CourseModel("Mathematics", "Prof. Johnson", "12:00 PM", "Room 102"));
        // Add more courses as needed
        return courses;
    }
    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Course())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();


    }
}

