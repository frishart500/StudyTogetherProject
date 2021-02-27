package com.example.kvantoriumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText subject, describtionOfTask;
    private Button postTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        init();

        postTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                getUser();
                intent.putExtra("subject", subject.getText().toString());
                intent.putExtra("describtion", describtionOfTask.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void init() {
        postTask = findViewById(R.id.postTask);
        subject = findViewById(R.id.subjectProblem);
        describtionOfTask = findViewById(R.id.describe);
    }

    private void getUser() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue(String.class);
                String subjectS = subject.getText().toString();
                String discribtionS = describtionOfTask.getText().toString();
                String img = snapshot.child("imgUri").getValue(String.class);
                String points;

                Task task = new Task(subjectS, discribtionS);
                task.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                task.setName(name);
                task.setImg(img);



                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Task");

                dr.push().setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateTaskActivity.this, "Задание загружено", Toast.LENGTH_SHORT).show();
                            System.out.println("Задание загружено");
                        } else {
                            Toast.makeText(CreateTaskActivity.this, "Задание не загркжено", Toast.LENGTH_SHORT).show();
                            System.out.println("Задание не загружено");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRefGetUid.addListenerForSingleValueEvent(eventListener);
    }

    private void showError(){}

}