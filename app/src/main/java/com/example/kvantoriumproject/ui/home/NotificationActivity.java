package com.example.kvantoriumproject.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kvantoriumproject.Friends;
import com.example.kvantoriumproject.ItemNotification;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    private ImageView back;
    private RecyclerView rv;
    private AdapterNotification adapter;
    private ArrayList<Friends> arrayList;
    private TextView my_notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().hide();
        rv = findViewById(R.id.rv);
        back = findViewById(R.id.back);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        arrayList = new ArrayList<Friends>();
        my_notifications = findViewById(R.id.my_notifications);
        my_notifications.setVisibility(View.VISIBLE);
        adapter = new AdapterNotification(this, arrayList);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        ValueEventListener val = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String myEmail = ds.child("myEmail").getValue(String.class);
                    String imgUri = ds.child("imgUri1").getValue(String.class);
                    String imgUri2 = ds.child("imgUri2").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String points = ds.child("points").getValue(String.class);
                    String phone = ds.child("phone").getValue(String.class);
                    String classText = ds.child("classText").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String nameOfTask = ds.child("nameOfTask").getValue(String.class);
                    String dateToFinish = ds.child("dateToFinish").getValue(String.class);
                    String subjectOfUser = ds.child("subjectOfUser").getValue(String.class);
                    String describtionOfUser = ds.child("describtionOfUser").getValue(String.class);
                    String describe = ds.child("describe").getValue(String.class);
                    String subject = ds.child("subject").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);
                    String userId = ds.child("userId").getValue(String.class);
                    String anotherId = ds.child("anotherId").getValue(String.class);
                    String idOfTask = ds.child("idOfTask").getValue(String.class);
                    String mUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    if(email.equals(mUserEmail)){
                        my_notifications.setVisibility(View.GONE);
                        //String subject, String describe, String name, String email, String points, String img, String phone, String nameOfTask, String dateToFinish,
                        // String classText, String myEmail, String id, String subjectOfUser, String describtionOfUser, String userId
                        arrayList.add(new Friends(subject, describe, name, email, points, "", phone, nameOfTask, dateToFinish, classText, myEmail, id, subjectOfUser, describtionOfUser, userId, idOfTask, anotherId, imgUri, imgUri2));
                        rv.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("Friends").addListenerForSingleValueEvent(val);

    }

    private void status(String status){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        User user = new User();
        user.setStatus(status);
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

}