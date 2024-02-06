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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Course #newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Course extends Fragment {

    private FloatingActionButton done;
    private EditText addProfessor, addVenue, addCourse;
    private NumberPicker numberPickerHourStart, numberPickerMinuteStart, numberPickerHourEnd, numberPickerMinuteEnd;
    private ToggleButton toggleButtonAmPmStart, toggleButtonAmPmEnd;
    private ToggleButton toggleButtonMonday, toggleButtonTuesday, toggleButtonWednesday, toggleButtonThursday, toggleButtonFriday;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_course, container, false);
        done = root.findViewById(R.id.floatingActionButton);
        addCourse = root.findViewById(R.id.editTextCourse);
        addProfessor = root.findViewById(R.id.editTextProfessor);
        addVenue = root.findViewById(R.id.editTextVenue);
        numberPickerHourStart = root.findViewById(R.id.numberPickerHour);
        numberPickerMinuteStart = root.findViewById(R.id.numberPickerMinuteSTART);
        numberPickerHourEnd = root.findViewById(R.id.numberPickerHourEND);
        numberPickerMinuteEnd = root.findViewById(R.id.numberPickerMinuteEND);
        toggleButtonAmPmStart = root.findViewById(R.id.toggleButtonAmPmSTART);
        toggleButtonAmPmEnd = root.findViewById(R.id.toggleButtonAmPmEND);
        toggleButtonMonday = root.findViewById(R.id.toggleButtonMonday);
        toggleButtonTuesday = root.findViewById(R.id.toggleButtonTuesday);
        toggleButtonWednesday = root.findViewById(R.id.toggleButtonWednesday);
        toggleButtonThursday = root.findViewById(R.id.toggleButtonThursday);
        toggleButtonFriday = root.findViewById(R.id.toggleButtonFriday);

        // Configure the number pickers (example for hour and minute)

        setNumberPickerFormatter(numberPickerHourStart);
        setNumberPickerFormatter(numberPickerMinuteStart);
        setNumberPickerFormatter(numberPickerHourEnd);
        setNumberPickerFormatter(numberPickerMinuteEnd);

        numberPickerHourStart.setMinValue(1);
        numberPickerHourStart.setMaxValue(12);

        numberPickerMinuteStart.setMinValue(0);
        numberPickerMinuteStart.setMaxValue(59);

        numberPickerMinuteEnd.setMinValue(1);
        numberPickerMinuteEnd.setMaxValue(59);

        numberPickerHourEnd.setMinValue(1);
        numberPickerHourEnd.setMaxValue(12);

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
        String courseName = addCourse.getText().toString();
        String professorName = addProfessor.getText().toString();
        String venue = addVenue.getText().toString();

        StringBuilder daysOfWeek = new StringBuilder();
        if (toggleButtonMonday.isChecked()) daysOfWeek.append("Monday ");
        if (toggleButtonTuesday.isChecked()) daysOfWeek.append("Tuesday ");
        if (toggleButtonWednesday.isChecked()) daysOfWeek.append("Wednesday ");
        if (toggleButtonThursday.isChecked()) daysOfWeek.append("Thursday ");
        if (toggleButtonFriday.isChecked()) daysOfWeek.append("Friday");

        String startTime = formatTime(numberPickerHourStart.getValue(), numberPickerMinuteStart.getValue(), toggleButtonAmPmStart.isChecked());
        String endTime = formatTime(numberPickerHourEnd.getValue(), numberPickerMinuteEnd.getValue(), toggleButtonAmPmEnd.isChecked());

        if (!courseName.isEmpty() && !professorName.isEmpty() && !venue.isEmpty() && !daysOfWeek.toString().isEmpty()) {
            if (isEndTimeValid(startTime, endTime)) {
                dbHelper.addCourse(courseName, professorName, venue, daysOfWeek.toString().trim(), startTime, endTime);
                Toast.makeText(getActivity(), "Course added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Invalid time input. Please ensure the inputted times are correct.", Toast.LENGTH_SHORT).show();
            }
            // Navigate back
        } else {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private String formatTime(int hour, int minute, boolean isPm) {
        String amPm = isPm ? "PM" : "AM";
        return String.format("%02d:%02d %s", hour, minute, amPm);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Hide the navigation bar
        hideNavigationBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Show the navigation bar
        showNavigationBar();
    }

    private void hideNavigationBar() {
        // Assuming you're using a BottomNavigationView
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottomNavView);
        if (navBar != null) {
            navBar.setVisibility(View.GONE);
        }
    }

    private void showNavigationBar() {
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottomNavView);
        if (navBar != null) {
            navBar.setVisibility(View.VISIBLE);
        }
    }

    // Function to check if the end time is later than the start time
    private boolean isEndTimeValid(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);

            return start != null && end != null && end.after(start);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}

