package com.example.studytogetherproject.slides;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studytogetherproject.R;

public class Slide6Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide6, container, false);
        TextView textStudy = view.findViewById(R.id.textStudy);
        textStudy.setText("учиться вместе \uD83C\uDF89\uD83C\uDF82");
        return view;
    }
}