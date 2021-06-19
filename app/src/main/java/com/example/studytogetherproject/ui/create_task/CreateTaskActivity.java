package com.example.studytogetherproject.ui.create_task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.Moduls.Task;
import com.example.studytogetherproject.Moduls.Users;
import com.example.studytogetherproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText describtionOfTask, nameOfTask, dateToFinish, classText, points;
    private CardView card1, card2, card3, card4, card5, card6;
    private FloatingActionButton fab, back_btn;
    private Button postTask;
    private String phone;
    private ImageView calendar, addPhotoForTask, taskImage, deleteImg, back;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imgUri;
    private Uri downloadUri;
    private TextView subject;
    private Snackbar snackbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().hide();
        init();
        createListOfTheSubjects();
        calendarBuilder();
        onClicks();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainLight));

    }
    private void onClicks() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card4.setVisibility(View.VISIBLE);
                card5.setVisibility(View.VISIBLE);
                card6.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.VISIBLE);
                postTask.setVisibility(View.VISIBLE);

                fab.setVisibility(View.GONE);
                card1.setVisibility(View.GONE);
                card2.setVisibility(View.GONE);
                card3.setVisibility(View.GONE);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card4.setVisibility(View.GONE);
                card5.setVisibility(View.GONE);
                card6.setVisibility(View.GONE);
                back_btn.setVisibility(View.GONE);
                postTask.setVisibility(View.GONE);

                fab.setVisibility(View.VISIBLE);
                card1.setVisibility(View.VISIBLE);
                card2.setVisibility(View.VISIBLE);
                card3.setVisibility(View.VISIBLE);
            }
        });

        addPhotoForTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Выберите картинку"), 123);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        postTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!describtionOfTask.getText().toString().isEmpty() && !nameOfTask.getText().toString().isEmpty()
                        && !dateToFinish.getText().toString().isEmpty() && !classText.getText().toString().isEmpty() && !points.getText().toString().isEmpty()
                        && !subject.getText().toString().equals("Предмет")) {
                    postingTask(v);
                    snackbar = Snackbar.make(v, "Опубликовано!", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();
                } else {
                    snackbar = Snackbar.make(v, "Введите всю информацию в пункты,чтобы опубликовать задание.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFF601C80);
                    snackbar.setTextColor(0XFFffffff);
                    snackbar.show();
                }
                if(!points.getText().toString().equals("")){
                    int countPoint = Integer.parseInt(points.getText().toString());
                    if (countPoint < 100) {
                        snackbar = Snackbar.make(v, "Цена за задние должна быть не меньше 100 и не больше 500 баллов.", Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(0XFF601C80);
                        snackbar.setTextColor(0XFFffffff);
                        snackbar.show();
                    }
                }
            }
        });
    }
    private void postingTask(View v) {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

        ValueEventListener val = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pointsUser = snapshot.child("points").getValue(String.class);
                String counterUser = snapshot.child("countOfHowMuchTasksCreated").getValue(String.class);

                int counterTasksCreated = Integer.parseInt(counterUser);
                int pointsCount = Integer.parseInt(pointsUser);
                int countPoint = Integer.parseInt(points.getText().toString());

                if (pointsCount < countPoint) {
                    snackbar = Snackbar.make(v, "У вас недостаточно баллов, чтобы опубликовать задание.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFF601C80);
                    snackbar.setTextColor(0XFFffffff);
                    snackbar.show();
                } else {
                    if (countPoint >= 100) {
                        postTask.setEnabled(true);
                        int count = pointsCount - countPoint;
                        int counterForTasks = counterTasksCreated + 1;
                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("countOfHowMuchTasksCreated").setValue(String.valueOf(counterForTasks));
                        Users users = new Users();
                        int rounded = (int) Math.round(count / 100.0) * 100;
                        int remainder = count - rounded;
                        int result = remainder + rounded;
                        users.setPoints(result + "");
                        FirebaseDatabase.getInstance().getReference("User")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("points").setValue(users.getPoints());

                        getUser();
                        if (counterForTasks == 1) {
                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("countOfHowMuchTasksCreated").setValue(String.valueOf(counterForTasks));
                            Dialog dialog;
                            dialog = new Dialog(getApplicationContext());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_first_task);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);

                            Button ok = dialog.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue(String.valueOf((pointsCount - countPoint) + 100));
                                }
                            });

                            dialog.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRefGetUid.addListenerForSingleValueEvent(val);
    }

    private void init() {
        fab = findViewById(R.id.fab_forward);
        back_btn = findViewById(R.id.back_btn);

        card1 = findViewById(R.id.card);
        card2 = findViewById(R.id.card1);
        card3 = findViewById(R.id.card2);
        card4 = findViewById(R.id.card3);
        card5 = findViewById(R.id.card4);
        card6 = findViewById(R.id.card5);

        addPhotoForTask = findViewById(R.id.addPhotoForTask);
        back = findViewById(R.id.back);
        taskImage = findViewById(R.id.taskImage);
        classText = findViewById(R.id.classOfUserInTask);
        calendar = findViewById(R.id.calendar);
        nameOfTask = findViewById(R.id.nameOfTask);
        postTask = findViewById(R.id.postTask);
        points = findViewById(R.id.points);
        subject = findViewById(R.id.subjectProblem);
        describtionOfTask = findViewById(R.id.describe);
        dateToFinish = findViewById(R.id.dateToFinish);
        deleteImg = findViewById(R.id.deleteImg);
    }

    private void createListOfTheSubjects() {
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CreateTaskActivity.this);
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

    private void calendarBuilder() {
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


                dateToFinish.setText(date);
            }
        };

        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateTaskActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void getUser() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                phone = snapshot.child("phone").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String imgUri1 = snapshot.child("imgUri").getValue(String.class);
                String subjectS = subject.getText().toString();
                String pointsForTask = points.getText().toString();
                String discribtionS = describtionOfTask.getText().toString();
                String img = snapshot.child("imgUri").getValue(String.class);
                String classTextS = classText.getText().toString();

                if (!subjectS.equals("Предмет") && !pointsForTask.isEmpty() && !discribtionS.isEmpty()) {

                    int countPointsForTask = Integer.parseInt(pointsForTask);
                    int rounded = (int) Math.round(countPointsForTask / 100.0) * 100;

                    String subject = snapshot.child("subject").getValue(String.class);
                    String describe = snapshot.child("describtion").getValue(String.class);

                    Task task = new Task(subjectS, discribtionS);
                    task.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    task.setName(name);
                    task.setImg(img);
                    task.setPhone(phone);
                    task.setId(id);
                    task.setImgUri1(imgUri1);
                    task.setPoints(rounded + "");
                    task.setNameOfTask(nameOfTask.getText().toString());
                    task.setDateToFinish(dateToFinish.getText().toString());
                    task.setClassText(classTextS);
                    task.setSubjectOfUser(subject);
                    task.setImg(String.valueOf(downloadUri));
                    task.setDescribtionOfUser(describe);

                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Task");
                    dr.push().setValue(task);
                    ValueEventListener valTask = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String s = "";
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                s = ds.getKey();
                                task.setIdOfTask(s);
                            }

                            dr.child(s).child("idOfTask").setValue(task.getIdOfTask());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("Task").addListenerForSingleValueEvent(valTask);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRefGetUid.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("TasksPhotos");
        if (requestCode == 123 && resultCode == RESULT_OK) {
            imgUri = data.getData();
            StorageReference imageReference = storageReference.child(imgUri.getLastPathSegment());
            UploadTask uploadTask = imageReference.putFile(imgUri);
            uploadTask = imageReference.putFile(imgUri);


            com.google.android.gms.tasks.Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, com.google.android.gms.tasks.Task<Uri>>() {
                @Override
                public com.google.android.gms.tasks.Task<Uri> then(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        taskImage.setVisibility(View.VISIBLE);
                        deleteImg.setVisibility(View.VISIBLE);
                        Picasso.get().load(downloadUri.toString()).into(taskImage);

                        deleteImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StorageReference photoRef = storage.getReferenceFromUrl(downloadUri.toString());
                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        snackbar = Snackbar.make(v, "Вы удалили картинку.", Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(0XFFffffff);
                                        snackbar.setTextColor(0XFF601C80);
                                        snackbar.show();
                                        taskImage.setVisibility(View.GONE);
                                        deleteImg.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });

                    }
                }
            });

        }
    }
}