package com.example.kvantoriumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView dataImg, back, userImg;
    private TextView regText, textOfImage;
    private Button reg;
    private EditText email, password, name, phone, data, describtion, subject;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference mDataBase;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private boolean isReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        init();
        setOnClickReg();
        getDataPicker();


        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // ночная тема не активна, используется светлая тема
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // ночная тема активна, и она используется
                regText.setTextColor(Color.parseColor("#FFFFFF"));
                textOfImage.setTextColor(Color.parseColor("#FFFFFF"));
                back.setColorFilter(getApplication().getResources().getColor(R.color.white));
                break;
        }

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
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");

        textOfImage = findViewById(R.id.textOfImage);
        regText = findViewById(R.id.textView2);
        back = findViewById(R.id.back);
        reg = findViewById(R.id.registrBtn);
        userImg = findViewById(R.id.chooseImg);
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
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
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

        if (!emailS.isEmpty() && !data.getText().toString().isEmpty() && !passwordS.isEmpty() && !name.getText().toString().isEmpty() && !describtion.getText().toString().isEmpty() && !subject.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()) {
            try {
                mAuth.createUserWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (uploadUri.toString() != null) {

                                User user = new User(email.getText().toString(), name.getText().toString(), data.getText().toString(), describtion.getText().toString(), phone.getText().toString(), subject.getText().toString(), uploadUri.toString(), "500");

                                FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(RegistrationActivity.this, "Успешно сохранено в базе данных!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);

                                        } else
                                            Toast.makeText(RegistrationActivity.this, "Ошибка. Не сохранено в базе данных!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(RegistrationActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                            } else if (uploadUri.toString() == null) {
                                Toast.makeText(RegistrationActivity.this, "Выберите картинку", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(RegistrationActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
            }

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
        } else if (subject.getText().toString().isEmpty()) {
            showError(subject, "Введите предмет");
        }
    }

    private void showError(EditText ed, String error) {
        ed.setError(error);
        ed.requestFocus();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && data != null && data.getData() != null) {
                if (resultCode == RESULT_OK) {
                    userImg.setImageURI(data.getData());
                    uploadImg();
                }
            }
        } catch (Exception e) {
        }
    }

    private void uploadImg() {

        try {

            Bitmap bitmap = ((BitmapDrawable) userImg.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            final StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + " my_img");
            UploadTask up = mRef.putBytes(byteArray);
            Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return mRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    uploadUri = task.getResult();
                }
            });
        } catch (Exception e) {
        }
    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }


}