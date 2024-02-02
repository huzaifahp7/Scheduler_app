package com.example.scheduler_app;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Exam#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Assignment extends Fragment {

    private FloatingActionButton done;
    private EditText addAssignment;
    private EditText addSubject;
    private EditText addDate;
    private EditText addTime;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_assignment, container, false);
        done = root.findViewById(R.id.floatingActionButton);
        addAssignment = root.findViewById(R.id.editTextUsername);
        addSubject = root.findViewById(R.id.editTextUsername1);
        addDate = root.findViewById(R.id.editTextUsername2);
        addTime = root.findViewById(R.id.editTextUsername3);
        dbHelper = new MyDatabaseHelper(getContext());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneClass();
            }
        });
        return root;
    }
    public void doneClass(){
        String course = addAssignment.getText().toString();
        String subject = addSubject.getText().toString();
        String date = addDate.getText().toString();
        String time = addTime.getText().toString();
        if (!course.isEmpty() && !subject.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
            // Add the assignment to the database
            dbHelper.addAssignment(course, subject, date, time);
            Toast.makeText(getActivity(), String.format("You have added %s for %s due on %s at %s.", course, subject, date, time), Toast.LENGTH_SHORT).show();
            // Pop back stack to return to the previous fragment
            getParentFragmentManager().popBackStack();
        } else {
            // Show a message if any field is empty
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
