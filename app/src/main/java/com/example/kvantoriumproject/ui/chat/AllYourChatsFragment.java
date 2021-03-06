package com.example.kvantoriumproject.ui.chat;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.Item;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.ui.dashboard.Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllYourChatsFragment extends Fragment {

    private AdapterChats adapter;
    private ArrayList<ItemChat> arrayList = new ArrayList<>();
    private RecyclerView rv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_all_chats, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Чаты");
        rv = root.findViewById(R.id.rv);
        buildRv();
        readAllUsers();

        return root;
    }

    private void buildRv() {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void readAllUsers() {

        ValueEventListener valueUserInf = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String emailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                ValueEventListener valueUsers = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = ds.child("name").getValue(String.class);
                            String email = ds.child("email").getValue(String.class);
                            String img = ds.child("img").getValue(String.class);
                            String subject = ds.child("subject").getValue(String.class);
                            String desc = ds.child("describe").getValue(String.class);

                            if(!emailUser.equals(email)){
                                arrayList.add(new ItemChat(name, img, email, desc, subject));

                                adapter = new AdapterChats(getContext(), arrayList);
                                rv.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                FirebaseDatabase.getInstance().getReference("Task").addListenerForSingleValueEvent(valueUsers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(valueUserInf);
    }
}