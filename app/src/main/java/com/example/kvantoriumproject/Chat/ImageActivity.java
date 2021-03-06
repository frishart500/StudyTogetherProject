package com.example.kvantoriumproject.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kvantoriumproject.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mImageView = findViewById(R.id.my_image_view);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        String img = getIntent().getStringExtra("image_id");
        if (extras != null) {
            Glide.with(mImageView).load(img).into(mImageView);
        }

    }
}