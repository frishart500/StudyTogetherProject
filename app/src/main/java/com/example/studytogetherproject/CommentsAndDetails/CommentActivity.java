package com.example.studytogetherproject.CommentsAndDetails;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studytogetherproject.Moduls.ChooseInf;
import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView rvComment;
    private ArrayList<ChooseInf> arrayList = new ArrayList<>();
    private AdapterForComments adapterForComments;
    private ImageView back;
    private String getEmail = null;
    private TextView noComment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back = findViewById(R.id.back);
        noComment = findViewById(R.id.noComment);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentActivity.this, MainActivity.class));
            }
        });

        getSupportActionBar().hide();
        getRaiting();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainLight));

    }

    private void getRaiting() {
        rvComment = findViewById(R.id.rvComment);
        rvComment.setHasFixedSize(true);
        rvComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String raiting = ds.child("raiting").getValue(String.class);
                    String comment = ds.child("comment").getValue(String.class);
                    getEmail = getIntent().getStringExtra("email");
                    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                    if (getEmail != null) {
                        if (email.equals(getEmail)) {
                            if (!mAuth.getUid().equals(email)) {
                                arrayList.add(new ChooseInf(name, comment, raiting));
                                adapterForComments = new AdapterForComments(getApplicationContext(), arrayList);
                                rvComment.setAdapter(adapterForComments);
                            }
                        }
                    }
                    if (getEmail == null) {
                        if (mAuth.getUid().equals(email)) {
                            arrayList.add(new ChooseInf(name, comment, raiting));
                            adapterForComments = new AdapterForComments(getApplicationContext(), arrayList);
                            rvComment.setAdapter(adapterForComments);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Raiting").addListenerForSingleValueEvent(valueEventListener);
    }

}