package com.example.studytogetherproject.ui.Notifications;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.studytogetherproject.Moduls.Friends;
import com.example.studytogetherproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {
    private RecyclerView rv;
    private AdapterNotification adapter;
    private ArrayList<Friends> arrayList;
    private TextView my_notifications;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        rv = view.findViewById(R.id.rv);
        my_notifications = view.findViewById(R.id.my_notifications);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<Friends>();

        my_notifications.setVisibility(View.VISIBLE);
        adapter = new AdapterNotification(getContext(), arrayList);

        Window window = ((Activity)getContext()).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.mainLight));

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

        return view;
    }
}