package com.example.kvantoriumproject.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.Item;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private Adapter adapter;
    private ArrayList<Item> arrayList = new ArrayList<>();
    private RecyclerView rv;
    private ImageView addTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Список заданий");
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

        readUser();

        return root;
    }


    private void readUser() {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subjectUser = snapshot.child("subject").getValue(String.class);
                System.out.println(subjectUser);
                ValueEventListener valueEventTask = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String subjectTask = null;
                        String describtion = null;
                        String nameTask = null;
                        String emailTask = null;
                        String imgPath = null;
                        String points = null;
                        String phone = null;

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            subjectTask = ds.child("subject").getValue(String.class);
                            System.out.println(subjectTask);
                            describtion = ds.child("describe").getValue(String.class);
                            nameTask = ds.child("name").getValue(String.class);
                            emailTask = ds.child("email").getValue(String.class);
                            imgPath = ds.child("img").getValue(String.class);
                            points = ds.child("points").getValue(String.class);
                            phone = ds.child("phone").getValue(String.class);

                            if (subjectTask != null) {
                                if (subjectUser.equalsIgnoreCase(subjectTask)) {
                                    arrayList.add(new Item(nameTask, imgPath,
                                            subjectTask, points,
                                            describtion, emailTask, phone));

                                    adapter = new Adapter(getContext(), arrayList);
                                    rv.setAdapter(adapter);
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                FirebaseDatabase.getInstance().getReference("Task").addListenerForSingleValueEvent(valueEventTask);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRef.addListenerForSingleValueEvent(eventListenerUser);
    }
}