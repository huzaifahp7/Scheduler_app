package com.example.scheduler_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public CourseAdapter(Context context, List<CourseModel> courses, int itemBackgroundColor) {
        this.context = context;
        this.courses = courses;
        this.itemBackgroundColor = itemBackgroundColor;
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
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameView, instructorView, startTime, endTime, venue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameView = itemView.findViewById(R.id.course_name);
            instructorView = itemView.findViewById(R.id.course_instructor);
            startTime = itemView.findViewById(R.id.course_starttime);
            endTime = itemView.findViewById(R.id.course_endtime);
            venue = itemView.findViewById(R.id.course_venue);
        }
    }
}




