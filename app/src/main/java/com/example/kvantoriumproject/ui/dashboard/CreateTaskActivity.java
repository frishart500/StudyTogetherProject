package com.example.kvantoriumproject.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Task;
import com.example.kvantoriumproject.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText subject, describtionOfTask, points;
    private Button postTask;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        init();

        getSupportActionBar().setTitle("Создание задания");

        postTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPoints();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                getUser();
                intent.putExtra("subject", subject.getText().toString());
                intent.putExtra("phone", phone);
                intent.putExtra("describtion", describtionOfTask.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void init() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        postTask = findViewById(R.id.postTask);
        points = findViewById(R.id.points);
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

                phone = snapshot.child("phone").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String subjectS = subject.getText().toString();
                String pointsForTask = points.getText().toString();
                String discribtionS = describtionOfTask.getText().toString();
                String img = snapshot.child("imgUri").getValue(String.class);

                if (subjectS.isEmpty()) {
                    showError(subject, "Введите предмет");
                }
                if (discribtionS.isEmpty()) {
                    showError(describtionOfTask, "Введите описание задания");
                }
                if (pointsForTask.isEmpty()) {
                    showError(points, "Введите очки за задание");
                } else if(!subjectS.isEmpty() && !pointsForTask.isEmpty() && !discribtionS.isEmpty()){
                    Task task = new Task(subjectS, discribtionS);
                    task.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    task.setName(name);
                    task.setImg(img);
                    task.setPhone(phone);
                    task.setPoints(pointsForTask);

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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRefGetUid.addListenerForSingleValueEvent(eventListener);
    }

    private void showError(EditText ed, String s){
        ed.requestFocus();
        ed.setError(s);
    }

    private void readPoints(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

        ValueEventListener valuePoint = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String points = snapshot.child("points").getValue(String.class);
                int pointsCount = Integer.parseInt(points);
                int count = pointsCount - 100;
                User user = new User();
                user.setPoints(count + "");
                FirebaseDatabase.getInstance().getReference("User")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("points").setValue(user.getPoints());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRefGetUid.addListenerForSingleValueEvent(valuePoint);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}