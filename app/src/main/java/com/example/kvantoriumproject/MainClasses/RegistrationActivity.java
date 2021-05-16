package com.example.kvantoriumproject.MainClasses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kvantoriumproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    //переменные
    private ImageView dataImg, back;
    private Button reg;
    private EditText email, password, name, phone, data, describtion;
    private TextView subject;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        init();
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(RegistrationActivity.this);
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
        setOnClickReg();
        getDataPicker();


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

                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    private void init() {
        back = findViewById(R.id.back);
        reg = findViewById(R.id.registrBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        describtion = findViewById(R.id.describe);
        phone = findViewById(R.id.phone);
        data = findViewById(R.id.editData);
        subject = findViewById(R.id.subject);
        dataImg = findViewById(R.id.dataImg);
    }

    private void setOnClickReg() {
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void registration(String emailS, String passwordS) {

        if (!emailS.isEmpty() && !data.getText().toString().isEmpty() && !passwordS.isEmpty() && !name.getText().toString().isEmpty() && !describtion.getText().toString().isEmpty() && !subject.getText().toString().equals("Твоя специализация") && !phone.getText().toString().isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), ChooseGenderActivity.class);
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("password", passwordS);
            putIntent(intent);
            startActivity(intent);

        } else if (emailS.isEmpty()) {
            showError(email, "Введите email");
        } else if (passwordS.isEmpty() && passwordS.length() < 6) {
            showError(password, "Введите пароль, который должен иметь не менее 6 символов");
        } else if (data.getText().toString().isEmpty()) {
            showError(data, "Введите дату рождения");
        } else if (describtion.getText().toString().isEmpty() && describtion.getText().toString().length() <= 60) {
            showError(describtion, "Введите описание, которое должно содержать менее 60 символов");
        } else if (phone.getText().toString().isEmpty()) {
            showError(phone, "Введите номер телефона");
        } else if (subject.getText().toString().equals("Твоя специализация")) {
            showError(subject, "Введите предмет");
        }
    }

    private void putIntent(Intent intent) {
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("data", data.getText().toString());
        intent.putExtra("describtion", describtion.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.putExtra("subject", subject.getText().toString());
        intent.putExtra("imgUri", "");
        intent.putExtra("points", "500");
        intent.putExtra("imgUri", "0");
        intent.putExtra("average", "5.0");
        intent.putExtra("howMuchTaskDone", "0");
        intent.putExtra("howMuchNotifications", "0");
    }

    private void showError(EditText ed, String error) {
        ed.setError(error);
        ed.requestFocus();
    }

    private void showError(TextView ed, String error) {
        ed.setError(error);
        ed.requestFocus();
    }

}