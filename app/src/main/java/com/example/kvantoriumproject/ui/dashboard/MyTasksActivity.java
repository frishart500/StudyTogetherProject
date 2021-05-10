package com.example.kvantoriumproject.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.kvantoriumproject.Moduls.Item;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Moduls.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyTasksActivity extends AppCompatActivity {
    private RecyclerView rv;
    private AdapterForMyTasks adapter;
    private ArrayList<Item> arrayList = new ArrayList<>();
    private TextView allTasks, my_tasks, textMainAct;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);
        getSupportActionBar().hide();
        init();
        recyclerBuilder();
        readMyTasks();
    }

    private void init() {
        rv = findViewById(R.id.rv);
        allTasks = findViewById(R.id.all_tasks);
        my_tasks = findViewById(R.id.my_tasks);
        textMainAct = findViewById(R.id.textMainAct);
        textMainAct.setVisibility(View.VISIBLE);

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_tasks.setTextSize(16);
                allTasks.setTextSize(19);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });
    }

    private void recyclerBuilder() {
        rv.setHasFixedSize(true);
        LinearLayoutManager lin = new LinearLayoutManager(getApplicationContext());
        lin.setReverseLayout(true);
        lin.setStackFromEnd(true);
        rv.setLayoutManager(lin);
    }

    private void readMyTasks() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ValueEventListener valueEventTask = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String subjectTask = ds.child("subject").getValue(String.class);
                            String id = ds.child("id").getValue(String.class);
                            String describtion = ds.child("describe").getValue(String.class);
                            String nameTask = ds.child("name").getValue(String.class);
                            String emailTask = ds.child("email").getValue(String.class);
                            String imgPath = ds.child("img").getValue(String.class);
                            String points = ds.child("points").getValue(String.class);
                            String phone = ds.child("phone").getValue(String.class);
                            String nameOfTask = ds.child("nameOfTask").getValue(String.class);
                            String dateToFinish = ds.child("dateToFinish").getValue(String.class);
                            String classText = ds.child("classText").getValue(String.class);
                            String subjectToDetail = ds.child("subjectOfUser").getValue(String.class);
                            String describeToDetail = ds.child("describtionOfUser").getValue(String.class);
                            String idOfTask = ds.child("idOfTask").getValue(String.class);
                            String imgUri1 = ds.child("imgUri1").getValue(String.class);

                            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(emailTask)){
                                if (subjectTask != null) {
                                        arrayList.add(new Item(nameTask, imgPath,
                                                subjectTask, points,
                                                describtion, emailTask, phone, nameOfTask, dateToFinish, classText, subjectToDetail, describeToDetail, id, idOfTask, imgUri1));

                                        adapter = new AdapterForMyTasks(getApplicationContext(), arrayList);
                                        rv.setAdapter(adapter);
                                    textMainAct.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {

        Dialog dialog;
        dialog = new Dialog(MyTasksActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exit_from_app_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView textCancel = dialog.findViewById(R.id.no);
        TextView yes = dialog.findViewById(R.id.yes);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyTasksActivity.this.finishAffinity();
                System.exit(0);

            }
        });

        dialog.show();

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

}