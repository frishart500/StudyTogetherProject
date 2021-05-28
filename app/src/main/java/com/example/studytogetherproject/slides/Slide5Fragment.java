package com.example.studytogetherproject.slides;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studytogetherproject.MainClasses.LoginActivity;
import com.example.studytogetherproject.R;

public class Slide5Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide5, container, false);

        TextView btnSkip = view.findViewById(R.id.btnSkip);
        TextView text = view.findViewById(R.id.text);
        text.setText("Затем подождите, когда второй\nчеловек нажмёт эту кнопку и \nзадание будет полностью\nзавершено \uD83E\uDDE0");
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return view;
    }
}