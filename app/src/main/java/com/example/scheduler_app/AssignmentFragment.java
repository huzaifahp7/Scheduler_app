package com.example.scheduler_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssignmentFragment extends Fragment {

    private FloatingActionButton add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        populateAssignments(view);
        add = view.findViewById(R.id.addAssignment);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });
        return view;
    }

    private void populateAssignments(View view) {
        LinearLayout dayContainer = view.findViewById(R.id.dayContainer);
        List<AssignmentModel> assignments = getAssignments();

        for (AssignmentModel assignment : assignments) {
            TextView assignmentView = new TextView(getContext());
            assignmentView.setText(assignment.getTitle());
            dayContainer.addView(assignmentView);
        }
    }

    private List<AssignmentModel> getAssignments() {
        List<AssignmentModel> assignments = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            assignments.add(new AssignmentModel("Assignment 1", LocalDate.now().toString(), "CS 2110"));
            assignments.add(new AssignmentModel("Assignment 2", LocalDate.now().plusDays(1).toString(), "CS 1332"));
        }

        return assignments;
    }

    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Exam())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();
    }
}

