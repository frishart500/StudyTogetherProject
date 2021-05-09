package com.example.kvantoriumproject.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Moduls.Users;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ChangesActivity extends AppCompatActivity {
    private EditText name, data, subject, phone, describtion;
    private ImageView back;
    private ImageView dataImg;
    private Button change;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String USER_KEY = "User";
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private boolean isReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);
        init();
        getDataPicker();

        getSupportActionBar().hide();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Профиль корректно изменен!", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(0XFFffffff);
                snackbar.setTextColor(0XFF601C80);
                snackbar.show();
                changes();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void init() {
        name = findViewById(R.id.name);
        data = findViewById(R.id.editData);
        subject = findViewById(R.id.subject);
        phone = findViewById(R.id.phone);
        describtion = findViewById(R.id.describe);
        dataImg = findViewById(R.id.dataImg);
        change = findViewById(R.id.change);

        myRef = FirebaseDatabase.getInstance().getReference("User");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void changes() {
        String nameS = name.getText().toString();
        String dataS = data.getText().toString();
        String subjectS = subject.getText().toString();
        String phoneS = phone.getText().toString();
        String describtionS = describtion.getText().toString();

        ValueEventListener vChangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = new Users();
                if (!nameS.isEmpty()) {
                    users.setName(nameS);
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(users.getName());
                }
                if (!subjectS.isEmpty()) {
                    users.setSubject(subjectS);
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subject").setValue(users.getSubject());
                }
                if (!describtionS.isEmpty()) {
                    users.setDescribtion(describtionS);
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("describtion").setValue(users.getDescribtion());
                }
                if (!phoneS.isEmpty()) {
                    users.setPhone(phoneS);
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone").setValue(users.getPhone());
                }
                if (!dataS.isEmpty()) {
                    users.setData(dataS);
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("data").setValue(users.getData());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(vChangeListener);


    }

    private void getDataPicker() {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                String m = String.valueOf(month);
                String d = String.valueOf(day);
                System.out.println("MONTH " + month);
                if (String.valueOf(day).length() == 1) {
                    d = ("0" + day);
                }

                if (String.valueOf(month).length() == 1) {
                    m = ("0" + month);
                }

                date = d + "." + m + "." + year;


                data.setText(date);
            }
        };

        dataImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ChangesActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
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