package com.example.kvantoriumproject.Chat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Moduls.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private ImageView mImageView, back;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mImageView = findViewById(R.id.my_image_view);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        String img = getIntent().getStringExtra("image_id");
        if (extras != null) {
            Picasso.get().load(img).into(mImageView);
        }

    }
}