package com.example.kvantoriumproject.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class ChangesActivity extends AppCompatActivity {
    private EditText name, data, subject, phone, describtion;
    private ImageView chooseImg;
    private ImageView dataImg;
    private Button change;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String USER_KEY = "User";
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private boolean isReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);
        init();
        getDataPicker();
//        changes();

        getSupportActionBar().hide();

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        chooseImg = findViewById(R.id.chooseImg);

        myRef = FirebaseDatabase.getInstance().getReference("User");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");

    }

    private void changes() {
        String nameS = name.getText().toString();
        String dataS = data.getText().toString();
        String subjectS = subject.getText().toString();
        String phoneS = phone.getText().toString();
        String describtionS = describtion.getText().toString();

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String emailString = dataSnapshot.child("email").getValue(String.class);
                String pointsString = dataSnapshot.child("points").getValue(String.class);
                String imgPath = dataSnapshot.child("imgUri").getValue(String.class);
                Picasso.get().load(imgPath).into(chooseImg);
                try {
                    User user = new User(emailString, nameS, dataS, describtionS, phoneS, subjectS, uploadUri.toString(), pointsString);
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                if (!nameS.isEmpty()) {
                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(user.getName());
                                }

                                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imgUri").setValue(user.getImgUri());

                                if (!dataS.isEmpty()) {
                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("data").setValue(user.getData());
                                }
                                if (!describtionS.isEmpty()) {
                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("describtion").setValue(user.getDescribtion());
                                }
                                if (!phoneS.isEmpty()) {
                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone").setValue(user.getPhone());
                                }
                                if (!subjectS.isEmpty()) {
                                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subject").setValue(user.getSubject());
                                }

                            } else {
                            }

                        }
                    });

                } catch (Exception ex) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 && data != null && data.getData() != null) || (requestCode != 1 && data == null && data.getData() == null)) {
            if (resultCode == RESULT_OK) {
                chooseImg.setImageURI(data.getData());
                uploadImg();
            }
        }
    }

    private void uploadImg() {
        Bitmap bitmap = ((BitmapDrawable) chooseImg.getDrawable()).getBitmap();
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
    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

}