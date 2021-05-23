package com.example.kvantoriumproject.ui.create_task;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvantoriumproject.MainClasses.RegistrationActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Moduls.Task;
import com.example.kvantoriumproject.Moduls.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import org.w3c.dom.Text;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class CreateTaskFragment extends Fragment {
    private EditText describtionOfTask, nameOfTask, dateToFinish, classText, points;
    private Button postTask;
    private String phone;
    private ImageView calendar, addPhotoForTask, taskImage, deleteImg;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imgUri;
    private Uri downloadUri;
    private TextView subject;
    private Snackbar snackbar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_task, container, false);
        init(root);
        createListOfTheSubjects();
        calendarBuilder();
        onClicks();
        return root;
    }

    private void onClicks() {
        addPhotoForTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Выберите картинку"), 123);
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
                int countPoint = Integer.parseInt(points.getText().toString());
                if (countPoint < 100) {
                    snackbar = Snackbar.make(v, "Цена за задние должна быть не меньше 100 и не больше 500 баллов.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(0XFF601C80);
                    snackbar.setTextColor(0XFFffffff);
                    snackbar.show();
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
                            dialog = new Dialog(getContext());
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

    private void init(View root) {
        addPhotoForTask = root.findViewById(R.id.addPhotoForTask);
        taskImage = root.findViewById(R.id.taskImage);
        classText = root.findViewById(R.id.classOfUserInTask);
        calendar = root.findViewById(R.id.calendar);
        nameOfTask = root.findViewById(R.id.nameOfTask);
        postTask = root.findViewById(R.id.postTask);
        points = root.findViewById(R.id.points);
        subject = root.findViewById(R.id.subjectProblem);
        describtionOfTask = root.findViewById(R.id.describe);
        dateToFinish = root.findViewById(R.id.dateToFinish);
        deleteImg = root.findViewById(R.id.deleteImg);
    }

    private void createListOfTheSubjects() {
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
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
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.item_for_list, R.id.textSubject, countryArray);
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

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
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