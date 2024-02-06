package com.example.scheduler_app;

import android.content.Context;
import android.content.res.Resources;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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
    private String currentSortOrder = "date";
    private FloatingActionButton add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        dbHelper.openDatabase();
        view.findViewById(R.id.sortByCourse).setOnClickListener(v -> populateAssignments(view,"course"));
        view.findViewById(R.id.sortByDate).setOnClickListener(v -> populateAssignments(view,"date"));
        populateAssignments(view, "date");
        add = view.findViewById(R.id.addAssignment);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });
        return view;
    }

    private List<AssignmentModel> getAssignments(String sortBy) {
        List<AssignmentModel> assignments = new ArrayList<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        currentSortOrder = sortBy;
        String orderBy;
        if ("course".equals(sortBy)) {
            orderBy = "LOWER(subject) ASC"; // Sort by subject (course name), ignoring case
        } else {
            orderBy = "date ASC, time ASC"; // Sort by date and time
        }
        Cursor cursor = dbHelper.getReadableDatabase().query(
                "Assignment", null, null, null, null, null, orderBy
        );

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
        Log.d("AssignmentFragment", "OrderBy: " + orderBy);
        cursor.close();
        return assignments;
    }
    private void populateAssignments(View view, String sortBy) {
        currentSortOrder = sortBy;
        LinearLayout assignmentsContainer = view.findViewById(R.id.assignmentsContainer);
        assignmentsContainer.removeAllViews(); // Clear existing views

        List<AssignmentModel> assignments = getAssignments(sortBy);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());

        for (AssignmentModel assignment : assignments) {
            TextView assignmentView = new TextView(getContext());
            assignmentView.setText("Course: " +  assignment.getSubject() + "\n" + "Assignment Name: " + assignment.getTitle() + " \n" + "Due date: " + assignment.getDate() + "\n" + "Due Time: " +assignment.getTime());
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.rubikegular);
            assignmentView.setBackground(getPriorityColor(requireContext(),assignment.getDate()));
            assignmentView.setTypeface(typeface, Typeface.BOLD); // Set text to bold
            assignmentView.setTextColor(Color.BLACK); // Set text color
            assignmentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            assignmentView.setPadding(25,5,0,5);


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
                        populateAssignments(getView(), currentSortOrder);
                    } else {
                        Log.e("AssignmentFragment", "Context or View is null");
                    }
                }
            });
            ImageView editIcon = new ImageView(getContext());
            editIcon.setImageResource(R.drawable.ic_assignment); // Replace with your pencil icon drawable
            editIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open edit dialog for this exam
                    openEditAssignmentDialog(assignment);
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

            // Style the TextView (padding, margins, textAppearance, etc.)
            LinearLayout iconLayout = new LinearLayout(getContext());
            iconLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams iconLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            iconLayout.setLayoutParams(iconLayoutParam);

            iconLayout.addView(editIcon);
            iconLayout.addView(deleteIcon);
            assignmentsContainer.addView(assignmentView);
            assignmentsContainer.addView(iconLayout); // Add the layout with icons
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            iconParams.setMargins(16, 16, 16, 16); // Adjust margins as needed
            editIcon.setLayoutParams(iconParams);
            deleteIcon.setLayoutParams(iconParams);
        }
    }
    private void openEditAssignmentDialog(final AssignmentModel ass) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Assignment");

        // Set up the input fields
        final EditText inputName = new EditText(getContext());
        inputName.setHint("Assignment Name");
        inputName.setText(ass.getTitle());

        final EditText inputCourse = new EditText(getContext());
        inputCourse.setHint("Course Name");
        inputCourse.setText(ass.getSubject());

        final EditText inputDate = new EditText(getContext());
        inputDate.setHint("Due Date (yyyy-MM-dd)");
        inputDate.setText(ass.getDate());

        final EditText inputTime = new EditText(getContext());
        inputTime.setHint("Due Time (hh-mm) AM/PM");
        inputTime.setText(ass.getTime());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputCourse);
        layout.addView(inputName);
        layout.addView(inputDate);
        layout.addView(inputTime);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the exam in the database
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
                dbHelper.updateAssignment(ass.getId(), inputName.getText().toString(),inputCourse.getText().toString(),
                        inputTime.getText().toString(),
                        inputDate.getText().toString());
                populateAssignments(getView(), currentSortOrder); // Refresh the list
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
            populateAssignments(view, currentSortOrder);
        }
    }


    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Assignment())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();
    }
}

