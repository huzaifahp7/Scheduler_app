package com.example.scheduler_app;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduler_app.CourseModel;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Context context;
    private List<CourseModel> courses;
    private int itemBackgroundColor;
    private MyDatabaseHelper dbHelper;
    private CourseUpdate listener;



    public CourseAdapter(Context context, List<CourseModel> courses, int itemBackgroundColor, MyDatabaseHelper dbHelper, CourseUpdate listener) {
        this.context = context;
        this.courses = courses;
        this.itemBackgroundColor = itemBackgroundColor;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        CourseModel course = courses.get(position);
        holder.courseNameView.setText("Course: " + course.getCourse());
        holder.instructorView.setText("Instructor: " + course.getInstructor());
        holder.startTime.setText("Start Time: " +course.getStartTime());
        holder.endTime.setText("End Time: " +course.getEndTime());
        holder.venue.setText("Venue: " + course.getVenue());
        holder.itemView.setBackgroundResource(itemBackgroundColor);

        holder.deleteButton.setOnClickListener(view -> {
            dbHelper.deleteCourse(course.getId());
            courses.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, courses.size());
        });

        // Set click listener for edit button
        holder.editButton.setOnClickListener(view -> openEditCourseDialog(course));
    }


    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameView, instructorView, startTime, endTime, venue;
        ImageView deleteButton;
        ImageView editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseNameView = itemView.findViewById(R.id.course_name);
            instructorView = itemView.findViewById(R.id.course_instructor);
            startTime = itemView.findViewById(R.id.course_starttime);
            endTime = itemView.findViewById(R.id.course_endtime);
            venue = itemView.findViewById(R.id.course_venue);
            deleteButton = itemView.findViewById(R.id.deleteIcon);
            editButton = itemView.findViewById(R.id.editIcon);
        }
    }
    private void openEditCourseDialog(final CourseModel course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Course");

        // Set up the input fields
        final EditText inputName = new EditText(context);
        inputName.setHint("Course Name");
        inputName.setText(course.getCourse());

        final EditText inputProf = new EditText(context);
        inputProf.setHint("Professor's Name");
        inputProf.setText(course.getInstructor());

        final EditText inputVenue = new EditText(context);
        inputVenue.setHint("Location of Class");
        inputVenue.setText(course.getVenue());

        final EditText inputStartTime = new EditText(context);
        inputStartTime.setHint("Start Time (HH-MM) AM/PM");
        inputStartTime.setText(course.getStartTime());

        final EditText endTime = new EditText(context);
        endTime.setHint("End Time (HH-MM) AM/PM");
        endTime.setText(course.getEndTime());

        // ... repeat for other attributes: professor, venue, etc. ...

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(inputProf);
        layout.addView(inputVenue);
        layout.addView(inputStartTime);
        layout.addView(endTime);
        // ... add other inputs to the layout ...

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Update the course in the database
            dbHelper.updateCourse(course.getId(),
                    inputName.getText().toString(),
                    inputProf.getText().toString(),
                    inputVenue.getText().toString(),
                    inputStartTime.getText().toString(),
                    endTime.getText().toString());
            // Notify the fragment to refresh the course list
            if (context instanceof CourseUpdate) {
                ((CourseUpdate) context).onCourseUpdated();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}




