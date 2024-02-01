package com.example.scheduler_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Class_Schedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Class_Schedule extends Fragment {

    private FloatingActionButton done;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_class__schedule, container, false);
        done = root.findViewById(R.id.floatingActionButton4);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneClass();
            }
        });
        return root;
    }
    public void doneClass(){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment()) // Replace ClassFragment with your actual class fragment
                .commit();
    }
}