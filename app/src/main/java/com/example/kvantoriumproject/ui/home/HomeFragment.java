package com.example.kvantoriumproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kvantoriumproject.ChangesActivity;
import com.example.kvantoriumproject.LoginActivity;
import com.example.kvantoriumproject.MainActivity;
import com.example.kvantoriumproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private ImageView imgExit, userImg;
    private TextView email, phone, points, data, name, subject;
    private FirebaseAuth mAuth;
    private Button changeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        changeBtn = root.findViewById(R.id.changeBtn);
        userImg = root.findViewById(R.id.userImg);
        email = root.findViewById(R.id.email);
        name = root.findViewById(R.id.name);
        data = root.findViewById(R.id.data);
        phone = root.findViewById(R.id.phone);
        subject = root.findViewById(R.id.subject);
        points = root.findViewById(R.id.points);
        readUser();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Профиль");
        imgExit = root.findViewById(R.id.exit);
        mAuth = FirebaseAuth.getInstance();
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangesActivity.class));
            }
        });

        return root;
    }

    private void readUser(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameS = dataSnapshot.child("name").getValue(String.class);
                String emailString = dataSnapshot.child("email").getValue(String.class);
                String pointsString = dataSnapshot.child("points").getValue(String.class);
                email.setText(emailString);
                System.out.println(nameS);
                name.setText(nameS);
                String expertSubjectString = dataSnapshot.child("subject").getValue(String.class);
                subject.setText(expertSubjectString);
                String phoneString = dataSnapshot.child("phone").getValue(String.class);
                phone.setText(phoneString);
                String dataString = dataSnapshot.child("data").getValue(String.class);
                String imgPath = dataSnapshot.child("imgUri").getValue(String.class);
                if(imgPath != null) {
                    Picasso.get().load(imgPath).into(userImg);
                }else {
                    userImg.setImageResource(R.drawable.user_img);
                }
                data.setText(dataString);
                points.setText(pointsString);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

}