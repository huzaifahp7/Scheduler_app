package com.example.scheduler_app;

import android.database.Cursor;
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

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        dbHelper.openDatabase();

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

    private List<AssignmentModel> getAssignments() {
        List<AssignmentModel> assignments = new ArrayList<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        Cursor cursor = dbHelper.getAllAssignments();

        if (cursor.moveToFirst()) {
            do {
                String name = "";
                String subject = "";
                String date = "";
                String time = "";

                int nameIndex = cursor.getColumnIndex("name");
                int subjectIndex = cursor.getColumnIndex("subject");
                int dateIndex = cursor.getColumnIndex("date");
                int timeIndex = cursor.getColumnIndex("time");

                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex);
                }
                if (subjectIndex != -1) {
                    subject = cursor.getString(subjectIndex);
                }
                if (dateIndex != -1) {
                    date = cursor.getString(dateIndex);
                }
                if (timeIndex != -1) {
                    time = cursor.getString(timeIndex);
                }
                assignments.add(new AssignmentModel(name, subject, date, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assignments;
    }
    private void populateAssignments(View view) {
        LinearLayout dayContainer = view.findViewById(R.id.dayContainer);
        dayContainer.removeAllViews(); // Clear existing views

        List<AssignmentModel> assignments = getAssignments();
        for (AssignmentModel assignment : assignments) {
            TextView assignmentView = new TextView(getContext());
            String displayText = assignment.getTitle() + " - " + assignment.getSubject() + " - " + assignment.getDate() + " - " + assignment.getTime();
            assignmentView.setText(displayText);
            dayContainer.addView(assignmentView);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            populateAssignments(view);
        }
    }


    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Assignment())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();
    }
}

