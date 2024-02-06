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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Exam#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Exam extends Fragment {

    private FloatingActionButton done;
    private EditText addExam;
    private EditText addVenue;
    private NumberPicker numberPickerHour, numberPickerMinute, numberPickerDay, numberPickerMonth, numberPickerYear;
    private ToggleButton AmPm;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add__exam, container, false);
        done = root.findViewById(R.id.floatingActionButton);
        addExam = root.findViewById(R.id.editTextExam);
        addVenue = root.findViewById(R.id.editTextVenue);
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
        String course = addExam.getText().toString();
        String venue = addVenue.getText().toString();
        int day = numberPickerDay.getValue();
        int month = numberPickerMonth.getValue();
        int year = numberPickerYear.getValue();

        int hour = numberPickerHour.getValue();
        int minute = numberPickerMinute.getValue();;
        String ampm = AmPm.isChecked() ? "PM" : "AM";

        String date = String.format("%04d-%02d-%02d", year, month, day);
        String time = String.format("%02d:%02d %s", hour, minute, ampm);


        if (!course.isEmpty() && !venue.isEmpty() && !ampm.isEmpty()&& isInputDateValid(year, month, day) && isInputTimeValid(year, month, day, hour, minute, ampm)) {
            // Add the assignment to the database
            dbHelper.addExam(course, time, date, venue);
            Toast.makeText(getActivity(), String.format("You have added the exam %s successfully. It is at %s %s, at %s.", course, date, time, venue), Toast.LENGTH_SHORT).show();
            // Pop back stack to return to the previous fragment
            getParentFragmentManager().popBackStack();
        } else {
            // Show a message if any field is empty
            Toast.makeText(getActivity(), "Please fill in all fields and ensure date and time are correct.", Toast.LENGTH_SHORT).show();
        }
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
    private boolean isInputDateValid(int year, int month, int day) {
        try {
            Calendar currentCalendar = Calendar.getInstance();
            Calendar inputCalendar = Calendar.getInstance();
            inputCalendar.set(year, month - 1, day); // Note: Calendar month is zero-based

            return !inputCalendar.before(currentCalendar);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Function to check if the inputted time is not before the current time
    private boolean isInputTimeValid(int year, int month, int day, int hour, int minute, String ampm) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
            String inputDateTime = String.format("%04d-%02d-%02d %02d:%02d %s", year, month, day, hour, minute, ampm);
            Date inputtedDateTime = sdf.parse(inputDateTime);

            Calendar currentCalendar = Calendar.getInstance();
            Calendar inputCalendar = Calendar.getInstance();
            inputCalendar.setTime(inputtedDateTime);

            return inputtedDateTime != null && !inputCalendar.before(currentCalendar);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}