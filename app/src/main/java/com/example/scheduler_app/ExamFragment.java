package com.example.scheduler_app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExamFragment extends Fragment {

    private FloatingActionButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        dbHelper.openDatabase();

        populateExams(view);

        List<ExamModel> exams = getExams();
        add = view.findViewById(R.id.addExam);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });

        return view;
    }
    private List<ExamModel> getExams() {
        List<ExamModel> exams = new ArrayList<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        Cursor cursor = dbHelper.getAllExams();

        if (cursor.moveToFirst()) {
            do {
                String name = "";
                String location = "";
                String date = "";
                String time = "";
                int id = -1;

                int idIndex = cursor.getColumnIndex("_id");
                int nameIndex = cursor.getColumnIndex("name");
                int locationIndex = cursor.getColumnIndex("location");
                int dateIndex = cursor.getColumnIndex("date");
                int timeIndex = cursor.getColumnIndex("time");

                if (idIndex != -1) {
                    id = cursor.getInt(idIndex);
                }
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex);
                }
                if (locationIndex != -1) {
                    location = cursor.getString(locationIndex);
                }
                if (dateIndex != -1) {
                    date = cursor.getString(dateIndex);
                }
                if (timeIndex != -1) {
                    time = cursor.getString(timeIndex);
                }
                exams.add(new ExamModel(name, date, time, location, id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return exams;
    }
    private void populateExams(View view) {
        LinearLayout examsContainer = view.findViewById(R.id.examsContainer);
        examsContainer.removeAllViews(); // Clear existing views

        List<ExamModel> exams = getExams();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());

        for (ExamModel exam : exams) {
            TextView examView = new TextView(getContext());
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.rubikegular);
            examView.setText("Course: " +  exam.getTitle() + "\n"  + "Date: " + exam.getDate() + "\n" + "Time: " + exam.getTime()+ "\n" + "Venue: " + exam.getLocation());
            examView.setBackgroundColor(getPriorityColour(exam.getDate()));
            examView.setTypeface(typeface, Typeface.BOLD); // Set text to bold
            examView.setTextColor(Color.BLACK); // Set text color
            examView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            ImageView deleteIcon = new ImageView(getContext());
            deleteIcon.setImageResource(android.R.drawable.ic_menu_delete); // Use a trash icon here
            deleteIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ExamFragment", "Deleting assignment with ID: " + exam.getId());
                    dbHelper.deleteExam(exam.getId());

                    // Refresh the fragment's view
                    if (getContext() != null) {
                        populateExams(getView());
                    } else {
                        Log.e("ExamFragment", "Context or View is null");
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
                    openEditExamDialog(exam);
                }
            });

            examView.setOnClickListener(new View.OnClickListener() {
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
            examsContainer.addView(examView);
            examsContainer.addView(iconLayout); // Add the layout with icons
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            iconParams.setMargins(16, 16, 16, 16); // Adjust margins as needed
            editIcon.setLayoutParams(iconParams);
            deleteIcon.setLayoutParams(iconParams);
        }

    }

    private int getPriorityColour(String dueDate) {
        if (dueDate == null || dueDate.isEmpty()) {
            return Color.GRAY; // Default color for invalid or empty dates
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Log.d("ExamFragment", "Due date string: " + dueDate);

            Date due = sdf.parse(dueDate);
            Calendar now = Calendar.getInstance();
            Calendar dueCal = Calendar.getInstance();
            dueCal.setTime(due);


            long daysUntilDue = (dueCal.getTimeInMillis() - now.getTimeInMillis()) / (24 * 60 * 60 * 1000);

            if (daysUntilDue <= 2) {
                return Color.RED;
            } else if (daysUntilDue <= 14) {
                return Color.YELLOW;
            } else {
                return Color.GREEN;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Color.GRAY; // Default color for parsing errors
        }
    }
    private void openEditExamDialog(final ExamModel exam) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Exam");

        // Set up the input fields
        final EditText inputName = new EditText(getContext());
        inputName.setHint("Exam Name");
        inputName.setText(exam.getTitle());

        final EditText inputLocation = new EditText(getContext());
        inputLocation.setHint("Location");
        inputLocation.setText(exam.getLocation());

        final EditText inputDate = new EditText(getContext());
        inputDate.setHint("Date (yyyy-MM-dd)");
        inputDate.setText(exam.getDate());

        final EditText inputTime = new EditText(getContext());
        inputTime.setHint("Time");
        inputTime.setText(exam.getTime());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(inputLocation);
        layout.addView(inputDate);
        layout.addView(inputTime);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the exam in the database
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
                dbHelper.updateExam(exam.getId(), inputName.getText().toString(),
                        inputTime.getText().toString(),
                        inputDate.getText().toString(),
                        inputLocation.getText().toString());
                populateExams(getView()); // Refresh the list
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



    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            populateExams(view);
        }
    }


    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_Exam())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();
    }


}

//start from here



