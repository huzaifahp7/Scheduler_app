package com.example.scheduler_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private CalendarView cV;
    private FloatingActionButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        cV = rootView.findViewById(R.id.calendarView);
        cV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                int dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);

                Fragment dayFragment = getDayFragment(dayOfWeek);
                if (dayFragment != null) {
                    // Now pass any additional data you need to the fragment (like assignments/exams due)
                    //loadAssignmentsAndExamsForDay(dayFragment, year, month, dayOfMonth);

                    // Open the fragment
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, dayFragment)
                            .commit();
                }
            }
        });
        return rootView;
    }

    private Fragment getDayFragment(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return new MondayFragment();
            case Calendar.TUESDAY:
                return new TuesdayFragment();
            case Calendar.WEDNESDAY:
                return new WednesdayFragment();
            case Calendar.THURSDAY:
                return new ThursdayFragment();
            case Calendar.FRIDAY:
                return new FridayFragment();
            default:
                return null;
        }
    }
//    private void loadAssignmentsAndExamsForDay(Fragment dayFragment, int year, int month, int day) {
//        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
//        String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
//
//        // Query for assignments and exams due on this date
//        // This is just an example, you'll need to implement these methods in your database helper
//        List<AssignmentModel> assignmentsDue = dbHelper.getAssignmentsDueOn(date);
//        List<ExamModel> examsDue = dbHelper.getExamsDueOn(date);
//
//        // Pass this data to the fragment
//        if (dayFragment instanceof DayFragmentInterface) {
//            ((DayFragmentInterface) dayFragment).setAssignmentsAndExams(assignmentsDue, examsDue);
//        }
//    }



    // If you need any additional setup or event handling, add it here.
    // For example, if you want to handle dates selected in the CalendarView,
    // you can set up a listener and handle the logic here.
}
