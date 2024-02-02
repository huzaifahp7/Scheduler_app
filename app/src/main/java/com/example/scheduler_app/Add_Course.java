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
public class Add_Course extends Fragment {

    private FloatingActionButton done;
    private EditText addClass;
    private EditText addProfessor;
    private EditText addLocation;
    private EditText addDays;
    private EditText addTime;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_course, container, false);
        done = root.findViewById(R.id.floatingActionButton);
        addClass = root.findViewById(R.id.editTextUsername);
        addProfessor = root.findViewById(R.id.editTextUsername1);
        addLocation = root.findViewById(R.id.editTextUsername4);
        addDays = root.findViewById(R.id.editTextUsername2);
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
        String course = addClass.getText().toString();
        String prof =addProfessor.getText().toString();
        String loc = addLocation.getText().toString();
        String days = addDays.getText().toString();
        String time = addTime.getText().toString();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Prepare the content values to be inserted
        ContentValues values = new ContentValues();
        values.put("course", course);
        values.put("professor", prof);
        values.put("time", time);
        values.put("date", days);
        values.put("location", loc);

        // Insert the values into the table
        long newRowId = database.insert("mytable", null, values);

        // Close the database connection
        database.close();
        Toast.makeText(getActivity(), String.format("You have taken %s of prof %s on %s %s at %s.", course, prof, time, days, loc), Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }
}
