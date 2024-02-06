package com.example.scheduler_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

public class ToDoFragment extends Fragment {

    private FloatingActionButton add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        dbHelper.openDatabase();

        populateToDos(view);
        add = view.findViewById(R.id.addToDo);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });
        return view;
    }

    private List<ToDoModel> getToDos() {
        List<ToDoModel> todos  = new ArrayList<>();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        Cursor cursor = dbHelper.getToDo();

        if (cursor.moveToFirst()) {
            do {
                String name = "";
                String date = "";
                String time = "";
                int id = -1;

                int idIndex = cursor.getColumnIndex("_id");
                int nameIndex = cursor.getColumnIndex("name");
                int dateIndex = cursor.getColumnIndex("date");
                int timeIndex = cursor.getColumnIndex("time");

                if (idIndex != -1) {
                    id = cursor.getInt(idIndex);
                }
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex);
                }
                if (dateIndex != -1) {
                    date = cursor.getString(dateIndex);
                }
                if (timeIndex != -1) {
                    time = cursor.getString(timeIndex);
                }
                todos.add(new ToDoModel(name, time, date, id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return todos;
    }
    private void populateToDos(View view) {
        LinearLayout todosContainer = view.findViewById(R.id.todosContainer);
        todosContainer.removeAllViews(); // Clear existing views

        List<ToDoModel> todos = getToDos();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());

        for (ToDoModel todo : todos) {
            TextView todoView = new TextView(getContext());
            todoView.setText("Task: " +  todo.getName() + "\n" + "Due date: " + todo.getDate() + "\n" + "Due Time: " + todo.getTime());
            todoView.setBackgroundColor(getPriorityColor(todo.getDate()));
            todoView.setTypeface(null, Typeface.BOLD); // Set text to bold
            todoView.setTextColor(Color.BLACK); // Set text color
            todoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);


            ImageView deleteIcon = new ImageView(getContext());
            deleteIcon.setImageResource(android.R.drawable.ic_menu_delete); // Use a trash icon here
            deleteIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("AssignmentFragment", "Deleting assignment with ID: " + todo.getId());
                    dbHelper.deleteToDo(todo.getId());

                    // Refresh the fragment's view
                    if (getContext() != null) {
                        populateToDos(getView());
                    } else {
                        Log.e("ToDoFragment", "Context or View is null");
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
                    openEditToDoDialog(todo);
                }
            });
            todoView.setOnClickListener(new View.OnClickListener() {
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

            LinearLayout iconLayout = new LinearLayout(getContext());
            iconLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams iconLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            iconLayout.setLayoutParams(iconLayoutParam);

            iconLayout.addView(editIcon);
            iconLayout.addView(deleteIcon);
            todosContainer.addView(todoView);
            todosContainer.addView(iconLayout); // Add the layout with icons
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            iconParams.setMargins(16, 16, 16, 16); // Adjust margins as needed
            editIcon.setLayoutParams(iconParams);
            deleteIcon.setLayoutParams(iconParams);
        }
    }

    private void openEditToDoDialog(final ToDoModel todo) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit To Do");

        // Set up the input fields
        final EditText inputName = new EditText(getContext());
        inputName.setHint("Task Name");
        inputName.setText(todo.getName());

        final EditText inputDate = new EditText(getContext());
        inputDate.setHint("Due Date (yyyy-MM-dd)");
        inputDate.setText(todo.getDate());

        final EditText inputTime = new EditText(getContext());
        inputTime.setHint("Due Time (hh-mm) AM/PM");
        inputTime.setText(todo.getTime());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
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
                dbHelper.updateToDo(todo.getId(), inputName.getText().toString(),
                        inputTime.getText().toString(),
                        inputDate.getText().toString());
                populateToDos(getView()); // Refresh the list
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

    private int getPriorityColor(String dueDate) {
        if (dueDate == null || dueDate.isEmpty()) {
            return Color.GRAY; // Default color for invalid or empty dates
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Log.d("TodoFragment", "Due date string: " + dueDate);

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


    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            populateToDos(view);
        }
    }


    public void AddClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Add_ToDo())
                .addToBackStack(null)// Replace ClassFragment with your actual class fragment
                .commit();
    }
}
