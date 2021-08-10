package study_com.studytogetherproject.Chat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import study_com.studytogetherproject.MainClasses.MainActivity;
import study_com.study_exmp.studytogetherproject.R;

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