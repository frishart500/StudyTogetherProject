package com.example.kvantoriumproject.slides;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kvantoriumproject.MainClasses.LoginActivity;
import com.example.kvantoriumproject.R;

public class Slide1Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slide1, container, false);
        TextView textView35 = view.findViewById(R.id.textView35);
        TextView btnSkip = view.findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        textView35.setText("- По окончании туториала вам дадут 200 баллов \uD83D\uDC8E\n" +
                "- Чтобы получать, баллы нужно выполнять задания \uD83D\uDCC8\n" +
                "- Чтобы выложить, задание нужно их тратить \uD83D\uDCC9\n" +
                "- За выполнение заданий эксперту ставится оценка, которая влияет на рейтинг \uD83D\uDD25");
        return view;

    }
}