package com.example.scheduler_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends Fragment {

    private FloatingActionButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);

        ListView listViewCourses = view.findViewById(R.id.listViewExams);
        List<ExamModel> exams = getExams();
        add = view.findViewById(R.id.addExam);

        ExamAdapter adapter = new ExamAdapter(getContext(), exams);
        listViewCourses.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });

        return view;
    }

    private List<ExamModel> getExams() {
        List<ExamModel> exams = new ArrayList<>();
        exams.add(new ExamModel("Math", "2024-03-10", "10:00 AM", "weber"));
        exams.add(new ExamModel("Science", "2024-03-15", "01:00 PM", "skiles"));
        // Add more exams as needed
        return exams;
    }

    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Exam())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();


    }

}
