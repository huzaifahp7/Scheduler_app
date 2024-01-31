package com.example.scheduler_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scheduler_app.CourseModel;

import java.util.List;

class CourseAdapter extends ArrayAdapter<CourseModel> {
    private Context context;
    private List<CourseModel> courses;

    public CourseAdapter(Context context, List<CourseModel> courses) {
        super(context, 0, courses);
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        CourseModel course = getItem(position);
        if (course != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            String courseText = course.getCourse() + "\nInstructor: " + course.getInstructor() +
                    "\nTime: " + course.getTime() + "\nLocation: " + course.getLocation();
            textView.setText(courseText);
        }
        return convertView;
    }
}