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
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    private NumberPicker numberPickerHour, numberPickerMinute, numberPickerDay, numberPickerMonth, numberPickerYear;
    private ToggleButton AmPm;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_assignment, container, false);
        done = root.findViewById(R.id.floatingActionButton);
        addAssignment = root.findViewById(R.id.editTextAssignment);
        addSubject = root.findViewById(R.id.editTextSubject);
        AmPm = root.findViewById(R.id.toggleButtonAmPm);
        numberPickerHour = root.findViewById(R.id.numberPickerHour);
        numberPickerMinute = root.findViewById(R.id.numberPickerMinute);
        numberPickerDay = root.findViewById(R.id.numberPickerDay);
        numberPickerMonth = root.findViewById(R.id.numberPickerMonth);
        numberPickerYear = root.findViewById(R.id.numberPickerYear);

        // Configure the number pickers (example for hour and minute)

        setNumberPickerFormatter(numberPickerHour);
        setNumberPickerFormatter(numberPickerMinute);
        setNumberPickerFormatter(numberPickerDay);
        setNumberPickerFormatter(numberPickerMonth);
        setNumberPickerFormatter(numberPickerYear);

        numberPickerHour.setMinValue(1);
        numberPickerHour.setMaxValue(12);

        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        numberPickerDay.setMinValue(1);
        numberPickerDay.setMaxValue(31);

        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setMaxValue(12);

        numberPickerYear.setMinValue(2023);
        numberPickerYear.setMaxValue(2026);

        dbHelper = new MyDatabaseHelper(getContext());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneClass();
            }
        });
        return root;
    }
    private void setNumberPickerFormatter(NumberPicker numberPicker) {
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
    }

    public void doneClass(){
        String course = addAssignment.getText().toString();
        String subject = addSubject.getText().toString();
        int day = numberPickerDay.getValue();
        int month = numberPickerMonth.getValue();
        int year = numberPickerYear.getValue();

        int hour = numberPickerHour.getValue();
        int minute = numberPickerMinute.getValue();;
        String ampm = AmPm.isChecked() ? "PM" : "AM";

        String date = String.format("%04d-%02d-%02d", year, month, day);
        String time = String.format("%02d:%02d %s", hour, minute, ampm);


        if (!course.isEmpty() && !subject.isEmpty() && !ampm.isEmpty()) {
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
