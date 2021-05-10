package com.example.kvantoriumproject.ui.friendsInChats;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.Moduls.ItemChat;
import com.example.kvantoriumproject.R;
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
    private EditText edit_find;
    private TextView my_chats;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_all_chats, container, false);
        getView(root);
        buildRv();
        readData();
        searchUsers();

        return root;
    }

    private void getView(View root) {
        rv = root.findViewById(R.id.rv);
        edit_find = root.findViewById(R.id.edit_find);
        my_chats = root.findViewById(R.id.my_chats);
        my_chats.setVisibility(View.VISIBLE);
    }

    private void searchUsers() {
        edit_find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void buildRv() {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void filter(String text) {
        ArrayList<ItemChat> array = new ArrayList<>();
        for (ItemChat item : arrayList) {
            if (item.getNameOfTask().toLowerCase().contains(text.toLowerCase())) {
                array.add(item);
            }
        }
        adapter.filterList(array);
    }

    private void readData() {
        ValueEventListener valUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ValueEventListener val = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = ds.child("name").getValue(String.class);
                            String nameAnotherUser = ds.child("nameAnotherUser").getValue(String.class);
                            String email = ds.child("email").getValue(String.class);
                            String img = ds.child("img").getValue(String.class);
                            String subject = ds.child("subject").getValue(String.class);
                            String desc = ds.child("describe").getValue(String.class);
                            String classText = ds.child("classText").getValue(String.class);
                            String nameOfTask = ds.child("nameOfTask").getValue(String.class);
                            String myEmail = ds.child("myEmail").getValue(String.class);
                            String phone = ds.child("phone").getValue(String.class);
                            String price = ds.child("points").getValue(String.class);
                            String describe = ds.child("describe").getValue(String.class);
                            String dateToFinish = ds.child("dateToFinish").getValue(String.class);
                            String userId = ds.child("userId").getValue(String.class);
                            String anotherId = ds.child("anotherId").getValue(String.class);
                            String idOfTask = ds.child("idOfTask").getValue(String.class);
                            String id = ds.child("id").getValue(String.class);
                            String imgUri1 = ds.child("imgUri1").getValue(String.class);
                            String imgUri2 = ds.child("imgUri2").getValue(String.class);

                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userId)) {
                                arrayList.add(new ItemChat(name, img, email, desc, subject, classText, nameOfTask, nameAnotherUser, phone, price, describe, dateToFinish, myEmail, userId, anotherId, idOfTask, id, imgUri1));
                                adapter = new AdapterChats(getContext(), arrayList);
                                rv.setAdapter(adapter);
                                my_chats.setVisibility(View.GONE);
                            } else if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(anotherId)) {
                                arrayList.add(new ItemChat(name, img, email, desc, subject, classText, nameOfTask, nameAnotherUser, phone, price, describe, dateToFinish, myEmail, userId, anotherId, idOfTask, id, imgUri2));
                                adapter = new AdapterChats(getContext(), arrayList);
                                rv.setAdapter(adapter);
                                my_chats.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                FirebaseDatabase.getInstance().getReference("FriendsInChats").addListenerForSingleValueEvent(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valUser);
    }

}