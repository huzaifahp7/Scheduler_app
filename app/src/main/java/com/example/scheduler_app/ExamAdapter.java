package com.example.scheduler_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scheduler_app.ExamModel;

import java.util.List;

class ExamAdapter extends ArrayAdapter<ExamModel> {
    private Context context;
    private List<ExamModel> exams;

    public ExamAdapter(Context context, List<ExamModel> exams) {
        super(context, 0, exams);
        this.context = context;
        this.exams = exams;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        ExamModel exam = getItem(position);
        if (exam != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            String examText = exam.getTitle() + "\nTime: " + exam.getDate() +
                    "\nLocation: " + exam.getLocation() + "\nTime: " + exam.getTime();
            textView.setText(examText);
        }
        return convertView;
    }
}
