package com.example.scheduler_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scheduler_app.ExamModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamModelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamModelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView textViewExamDate;
    private TextView textViewExamTime;
    private TextView textViewExamLocation;

    public ExamModelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param examModel Parameter 1
     * @return A new instance of fragment ExamModel.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamModelFragment newInstance(ExamModel examModel) {
        ExamModelFragment fragment = new ExamModelFragment();
        Bundle args = new Bundle();
        args.putSerializable("EXAM_MODEL", examModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_exam_model, container, false);

            // Initialize UI components
            textViewExamDate = view.findViewById(R.id.textViewExamDate);
            textViewExamTime = view.findViewById(R.id.textViewExamTime);
            textViewExamLocation = view.findViewById(R.id.textViewExamLocation);

            // Retrieve ExamModel data from arguments
            Bundle args = getArguments();
            if (args != null) {
                ExamModel examModel = (ExamModel) args.getSerializable("EXAM_MODEL");
                if (examModel != null) {
                    // Set data to UI components
                    updateUI(examModel);
                }
            }

            return view;
        }
    private void updateUI(ExamModel examModel) {
        textViewExamDate.setText("Date: " + examModel.getDate());
        textViewExamTime.setText("Time: " + examModel.getTime());
        textViewExamLocation.setText("Location: " + examModel.getLocation());
    }
}

