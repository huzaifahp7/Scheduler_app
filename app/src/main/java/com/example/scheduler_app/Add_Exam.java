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
public class Add_Exam extends Fragment {

    private FloatingActionButton done;
    private EditText addExam;
    private EditText addLocation;
    private EditText addDate;
    private EditText addTime;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add__exam, container, false);
        done = root.findViewById(R.id.floatingActionButton);
        addExam = root.findViewById(R.id.editTextUsername);
        addLocation = root.findViewById(R.id.editTextUsername3);
        addDate = root.findViewById(R.id.editTextUsername1);
        addTime = root.findViewById(R.id.editTextUsername2);

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
        String course = addExam.getText().toString();
        String loc = addLocation.getText().toString();
        String date = addDate.getText().toString();
        String time = addTime.getText().toString();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Prepare the content values to be inserted
        //Need to make separate sql table for all fragments
        ContentValues values = new ContentValues();
        values.put("course", course);
        values.put("time", time);
        values.put("date", date);
        values.put("location", loc);

        // Insert the values into the table
        long newRowId = database.insert("mytable", null, values);

        // Close the database connection
        database.close();
        Toast.makeText(getActivity(), String.format("You have a %s Exam on %s %s at %s.", course, date, time, loc), Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }
}