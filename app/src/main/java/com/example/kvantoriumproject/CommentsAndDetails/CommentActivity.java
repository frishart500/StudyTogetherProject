package com.example.kvantoriumproject.CommentsAndDetails;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kvantoriumproject.Moduls.ChooseInf;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Moduls.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView rvComment;
    private ArrayList<ChooseInf> arrayList = new ArrayList<>();
    private AdapterForComments adapterForComments;
    private ImageView back;
    private String getEmail = null;
    private String getMyEmail = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentActivity.this, MainActivity.class));
            }
        });

        getSupportActionBar().hide();
        getRaiting();
    }

    private void status(String status){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Users users = new Users();
        users.setStatus(status);
        ref.child("status").setValue(status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }


    private void getRaiting() {
        rvComment = findViewById(R.id.rvComment);
        rvComment.setHasFixedSize(true);
        rvComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
/*                User user = new User();
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;
                double ratingInDoubleVar;*/
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String myEmail = ds.child("myEmail").getValue(String.class);
                    String raiting = ds.child("raiting").getValue(String.class);
                    String comment = ds.child("comment").getValue(String.class);
                    //ratingInDoubleVar = Double.parseDouble(raiting);

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
/*                average = total / count;
                System.out.println(average);
                user.setAverage(String.valueOf(average));*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Raiting").addListenerForSingleValueEvent(valueEventListener);
    }

}