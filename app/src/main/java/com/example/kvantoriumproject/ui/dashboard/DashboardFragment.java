package com.example.kvantoriumproject.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.Adapter;
import com.example.kvantoriumproject.CreateTaskActivity;
import com.example.kvantoriumproject.Item;
import com.example.kvantoriumproject.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.internal.cache.DiskLruCache;

public class DashboardFragment extends Fragment {
    private Adapter adapter;
    private ArrayList<Item> arrayList = new ArrayList<>();
    private RecyclerView rv;
    private ImageView addTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Список экспертов");
        rv = root.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        addTask = root.findViewById(R.id.addTask);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateTaskActivity.class));
            }
        });


        readUsers();
        return root;
    }

    private void readUsers() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User");
        final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);
        // final DatabaseReference subjectTask = rootRef.child("User").child(uid).child("Task").child("subject");
        ValueEventListener eventListenerGetProfileData = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameSS = snapshot.child("name").getValue(String.class);
                String subjectSS = snapshot.child("subject").getValue(String.class);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String nameS = ds.child("name").getValue(String.class);
                            String imgPath = ds.child("imgUri").getValue(String.class);
                            String email = ds.child("email").getValue(String.class);

                            if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                arrayList.add(new Item(nameS, imgPath,
                                        "subject", "100",
                                        "describtion", email));

                                adapter = new Adapter(getContext(), arrayList);
                                rv.setAdapter(adapter);
                            }

                         /*   if (!nameS.equals(nameSS)) {
                                String subject = getActivity().getIntent().getStringExtra("subject");
                                if (subject != null) {
                                    if(subject.equalsIgnoreCase(subjectSS)) {
                                        String describtion = getActivity().getIntent().getStringExtra("describtion");
                                        arrayList.add(new Item(nameS, imgPath,
                                            subject, "100",
                                            describtion));
                                    }
                                }else
                                    arrayList.clear();
                            }
                            adapter = new Adapter(getContext(), arrayList);
                            rv.setAdapter(adapter);*/
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                uidRef.addListenerForSingleValueEvent(eventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRefGetUid.addListenerForSingleValueEvent(eventListenerGetProfileData);
    }

}