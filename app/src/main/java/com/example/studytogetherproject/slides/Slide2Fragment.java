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

public class Slide2Fragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide2, container, false);
        TextView text = view.findViewById(R.id.text);
        TextView btnSkip = view.findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        text.setText("- Публикуете задание \uD83D\uDCDD\n" +
                "- У вас списывают баллы \uD83D\uDCB8\n" +
                "- Эксперты кидают заявки \uD83D\uDC68\u200D\uD83C\uDF93\n" +
                "- Выбираете эксперта \uD83D\uDD0D\n" +
                "- Эксперт помогает вам\uD83E\uDD1D\n" +
                "- Эксперт получает баллы \uD83D\uDC8E\n" +
                "- Пишите отзыв о работе \uD83D\uDCC3");
        return view;
    }
}