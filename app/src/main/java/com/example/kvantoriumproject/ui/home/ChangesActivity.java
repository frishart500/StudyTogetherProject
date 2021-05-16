package com.example.kvantoriumproject.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private EditText name, data, phone, describtion;
    private ImageView back;
    private ImageView dataImg;
    private Button change;
    private TextView subject;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);
        init();
        getDataPicker();
        createListOfTheSubjects();
        getSupportActionBar().hide();

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

    private void createListOfTheSubjects(){
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ChangesActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.list_of_subject);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                ListView list = dialog.findViewById(R.id.list);
                ImageView close = dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                String[] countryArray = {"Алгебра", "Англ. яз.", "Биология", "География",
                        "Геометрия", "Информатика", "Искусство", "История", "Литература", "Немецкий язык", "ОБЖ", "Обществознание",
                        "Русский язык", "Физика", "Физкультура", "Химия"};
                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_for_list, R.id.textSubject, countryArray);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        subject.setText(countryArray[position]);
                    }
                });

                dialog.show();
            }
        });
    }

    private void init() {
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        data = findViewById(R.id.editData);
        subject = findViewById(R.id.subject);
        phone = findViewById(R.id.phone);
        describtion = findViewById(R.id.describe);
        dataImg = findViewById(R.id.dataImg);
        change = findViewById(R.id.change);
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
                if (!subjectS.equals("")) {
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


}