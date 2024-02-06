package com.example.scheduler_app;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                int id = -1;

                int idIndex = cursor.getColumnIndex("_id");
                int nameIndex = cursor.getColumnIndex("name");
                int subjectIndex = cursor.getColumnIndex("subject");
                int dateIndex = cursor.getColumnIndex("date");
                int timeIndex = cursor.getColumnIndex("time");

                if (idIndex != -1) {
                    id = cursor.getInt(idIndex);
                }
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
                assignments.add(new AssignmentModel(name, subject, date, time, id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assignments;
    }
    private void populateAssignments(View view) {
        LinearLayout assignmentsContainer = view.findViewById(R.id.assignmentsContainer);
        assignmentsContainer.removeAllViews(); // Clear existing views

        List<AssignmentModel> assignments = getAssignments();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());

        for (AssignmentModel assignment : assignments) {
            TextView assignmentView = new TextView(getContext());
            assignmentView.setText("Course: " +  assignment.getSubject() + "\n" + "Assignment Name: " + assignment.getTitle() + " \n" + "Due date: " + assignment.getDate() + "\n" + "Due Time: " +assignment.getTime());

            assignmentView.setBackground(getPriorityColor(requireContext(),assignment.getDate()));
            assignmentView.setTypeface(null, Typeface.BOLD); // Set text to bold
            assignmentView.setTextColor(Color.BLACK); // Set text color
            assignmentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            // Style the TextView (padding, margins, textAppearance, etc.)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 16);
            assignmentView.setLayoutParams(layoutParams);
            assignmentView.setPadding(20, 20, 20, 20);

            ImageView deleteIcon = new ImageView(getContext());
            deleteIcon.setImageResource(R.drawable.ic_menu_delete); // Use a trash icon here
            deleteIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ViewGroup.MarginLayoutParams layoutParames = (ViewGroup.MarginLayoutParams) deleteIcon.getLayoutParams();

// Set the bottom margin
            layoutParames.bottomMargin = 26; // Adjust the margin value as needed

// Apply the updated layout parameters
            deleteIcon.setLayoutParams(layoutParames);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AssignmentFragment", "Deleting assignment with ID: " + assignment.getId());
                    dbHelper.deleteAssignment(assignment.getId());

                    // Refresh the fragment's view
                    if (getContext() != null) {
                        populateAssignments(getView());
                    } else {
                        Log.e("AssignmentFragment", "Context or View is null");
                    }
                }
            });
            assignmentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if the view is already enlarged
                    boolean isEnlarged = v.getTag() != null && (boolean) v.getTag();
                    v.setTag(!isEnlarged); // Toggle the state

                    if (isEnlarged) {
                        // If enlarged, reduce size
                        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else {
                        // If not enlarged, increase size
                        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f));
                        ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    }
                }
            });

            assignmentsContainer.addView(assignmentView);
            assignmentsContainer.addView(deleteIcon);
        }
    }

    private Drawable getPriorityColor(Context context,String dueDate) {
        if (dueDate == null || dueDate.isEmpty()) {
            return ContextCompat.getDrawable(context,R.drawable.rounded_corner_grey); // Default color for invalid or empty dates
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Log.d("AssignmentFragment", "Due date string: " + dueDate);

            Date due = sdf.parse(dueDate);
            Calendar now = Calendar.getInstance();
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(due);


            long daysUntilDue = (dueCal.getTimeInMillis() - now.getTimeInMillis()) / (24 * 60 * 60 * 1000);

            if (daysUntilDue <= 2) {
                return ContextCompat.getDrawable(context,R.drawable.rounded_cor_red_icon);
            } else if (daysUntilDue <= 14) {
                return ContextCompat.getDrawable(context,R.drawable.rounded_cor_yellow_icon);
            } else {
                return ContextCompat.getDrawable(context,R.drawable.rounded_cor_green_icon);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ContextCompat.getDrawable(context,R.drawable.rounded_corner_grey); // Default color for parsing errors
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

