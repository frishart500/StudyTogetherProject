package com.example.studytogetherproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studytogetherproject.Moduls.FinishTask;
import com.example.studytogetherproject.MainClasses.MainActivity;
import com.example.studytogetherproject.R;
import com.example.studytogetherproject.Moduls.Users;
import com.example.studytogetherproject.ui.friendsInChats.AllYourChatsFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.studytogetherproject.notificationPack.APIService;
import com.example.studytogetherproject.notificationPack.Client;
import com.example.studytogetherproject.notificationPack.Data;
import com.example.studytogetherproject.notificationPack.MyResponse;
import com.example.studytogetherproject.notificationPack.NotificationSender;
import com.example.studytogetherproject.notificationPack.Token;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    //List
    private ListView messageListView;
    private AwesomeMessageAdapter adapter;

    private Button sendBtn, addPhotoBtn, btnFinish;
    private EditText messageEditText;

    private String nameTitle, phoneTitle, classTextTitle, describeTitle, priceTitle, dateToFinishTitle, id_str, id, justId, anotherId;

    private ImageView back, callImage;
    private CircleImageView userImgChat;
    private TextView nameInChats, classTextInChats, describe, price, dateToFinish;
    //расширяемая карточка
    private ConstraintLayout cl;
    private CardView cardView;
    //для уведомлкений
    public static final String CHANNEL_ID = "school";
    private APIService apiService;
    //переменные для взаимодействованияс Firebase
    private ChildEventListener childEventListener;
    private DatabaseReference dr;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference ref;
    private ValueEventListener seenListener;
    private FirebaseAuth mAuth;
    private DateTimeFormatter dateFprmat, timeFormat;
    private AwesomeMessage message;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init(); // иницилизация
        getIntentExtra(); // получение intent

        List<AwesomeMessage> awesomeMessages = new ArrayList<>(); // списиок
        adapter = new AwesomeMessageAdapter(this, R.layout.message_item, awesomeMessages);
        messageListView.setAdapter(adapter);
        classTextInChats.setText(classTextTitle + " " + getResources().getString(R.string.grade_down));
        setChatAdapter();

        onClick();
        UpdateToken();
        createNotificationChannel();

        if (mAuth.getCurrentUser().getUid().equals(id)) {
            seenMessage(anotherId);
        } else if (mAuth.getCurrentUser().getUid().equals(anotherId)) {
            seenMessage(id);
        }

        getImages();

    }


    private void setChatAdapter() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                message = dataSnapshot.getValue(AwesomeMessage.class);
                //если полученный предмет равен предмету в YourMessages
                if (message.getIdOfTask().equals(justId)) {
                    if (message.getSender().equals(mAuth.getCurrentUser().getUid())
                            && message.getRecipient().equals(id) || message.getSender().equals(mAuth.getCurrentUser().getUid()) &&
                            message.getRecipient().equals(anotherId)) {
                        message.setMine(true);
                        //добавлть items в adapter
                        adapter.add(message);
                    }
                    if (message.getRecipient().equals(mAuth.getCurrentUser().getUid())
                            && message.getSender().equals(id) || message.getRecipient().equals(mAuth.getCurrentUser().getUid()) &&
                            message.getSender().equals(anotherId)) {
                        message.setMine(false);
                        adapter.add(message);

                    }
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dr.addChildEventListener(childEventListener);
    }

    private void getImages(){
        if(mAuth.getCurrentUser().getUid().equals(anotherId)){
            FirebaseDatabase.getInstance().getReference("User").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String img = snapshot.child("imgUri").getValue(String.class);
                    Glide.with(getApplicationContext()).load(img).into(userImgChat);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if(mAuth.getCurrentUser().getUid().equals(id)){
            FirebaseDatabase.getInstance().getReference("User").child(anotherId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String img = snapshot.child("imgUri").getValue(String.class);
                    Glide.with(getApplicationContext()).load(img).into(userImgChat);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void getIntentExtra() {
        nameTitle = getIntent().getStringExtra("name");
        anotherId = getIntent().getStringExtra("anotherId");
        describeTitle = getIntent().getStringExtra("describeTask");
        classTextTitle = getIntent().getStringExtra("classText");
        phoneTitle = getIntent().getStringExtra("phone");
        priceTitle = getIntent().getStringExtra("price");
        id = getIntent().getStringExtra("userId");
        dateToFinishTitle = getIntent().getStringExtra("dateToFinish");
        justId = getIntent().getStringExtra("justId");
        id_str = getIntent().getStringExtra("id");
        //измение текста
        describe.setText(describeTitle);
        price.setText(priceTitle);
        dateToFinish.setText(dateToFinishTitle);
        nameInChats.setText(nameTitle);
    }

    private void seenMessage(String userId) {
        ref = FirebaseDatabase.getInstance().getReference("YourMessages");
        seenListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AwesomeMessage message = ds.getValue(AwesomeMessage.class);
                    if (message.getRecipient().equals(mAuth.getCurrentUser().getUid()) && message.getSender().equals(userId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen", true);
                        ds.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void init(){
        getSupportActionBar().hide();//базавая насройка(убрать action bar)
        userImgChat = findViewById(R.id.imgUserChat);
        describe = findViewById(R.id.describtionInChat);
        dateToFinish = findViewById(R.id.dateToFinishInChat);
        price = findViewById(R.id.priceInChat);
        callImage = findViewById(R.id.callToSm);
        btnFinish = findViewById(R.id.btnFinishInChat);
        cl = findViewById(R.id.cl);
        cardView = findViewById(R.id.card);
        messageListView = findViewById(R.id.list);
        sendBtn = findViewById(R.id.sendMessage);
        addPhotoBtn = findViewById(R.id.addPhoto);
        messageEditText = findViewById(R.id.messageEditText);
        back = findViewById(R.id.back);
        nameInChats = findViewById(R.id.nameInChats);
        classTextInChats = findViewById(R.id.classTextInChats);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class); //получение клиента

        //получение ветки БД
        dr = FirebaseDatabase.getInstance().getReference("YourMessages");
        mAuth = FirebaseAuth.getInstance();
    }
    //изменение статуса пользователя
    private void status(String status) {
        //получение из ветки User из уникального id пользователя, статус
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid());
        Users users = new Users();
        users.setStatus(status);
        ref.child("status").setValue(status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");//при возобновлении работы приложения статус меняется на online
    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.removeEventListener(seenListener);
        status("offline");//при остановке работы приложения статус меняется на offline
    }

    private void onClick() {
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {//если символов в сообщении больше 0 то кнопка активна
                    sendBtn.setEnabled(true);
                } else {
                    sendBtn.setEnabled(false);//если символов в сообщении меньше или = 0 то кнопка пассивна
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        messageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(500)
        });

        View.OnClickListener BTNs = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sendMessage) {

                    //при отправке сообщения
                    final String uid = mAuth.getCurrentUser().getUid();
                    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    //из ветки User получаем уникальный ключ текущего пользвателя
                    final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

                    ValueEventListener eventListener = new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //с помощью метода ValueEventListener получаем имя текущего польователя
                            String name = snapshot.child("name").getValue(String.class);
                            //объект message и его изменение
                            message = new AwesomeMessage();

                            dateFprmat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                            timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                            LocalDate localDate = LocalDate.now();
                            LocalTime localTime = LocalTime.now();

                            message.setText(messageEditText.getText().toString().trim());
                            message.setDate(dateFprmat.format(localDate));
                            message.setTime(timeFormat.format(localTime));
                            message.setName(name);
                            message.setSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            message.setIdOfTask(justId);
                            message.setRecipient(id);
                            message.setSeen(false);
                            //id текущего равен id полученного то
                            if (mAuth.getCurrentUser().getUid().equals(id)) {
                                //получить другой id пользователя и отправить ему уведомление
                                message.setRecipient(anotherId);
                                //получение из ветки Tokens token по которому забит пользователь
                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(anotherId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        ValueEventListener valStatus = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String status = snapshot.child("status").getValue(String.class);//получение статуса
                                                if (status.equals("offline")) {//если статус пользователя offline то отправить уведомление с сообщением
                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                    sendNotifications(usertoken, name, message.getText());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        };
                                        FirebaseDatabase.getInstance().getReference().child("User").child(anotherId).addListenerForSingleValueEvent(valStatus);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            //если пользователь равен полученному anotherId то
                            else if (anotherId.equals(mAuth.getCurrentUser().getUid())) {
                                //меняем получателя на полученный id
                                message.setRecipient(id);
                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(id).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ValueEventListener valStatus = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String status = snapshot.child("status").getValue(String.class);
                                                if (status.equals("offline")) {
                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                    sendNotifications(usertoken, name, message.getText());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        };
                                        FirebaseDatabase.getInstance().getReference().child("User").child(id).addListenerForSingleValueEvent(valStatus);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                Log.d("notification", "no one have notification");
                            }
                            //дополняем ветку YourMessages
                            dr.push().setValue(message)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                            //сразу после отправки сообщения editText меняет текст на ""
                            messageEditText.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };

                    uidRefGetUid.addListenerForSingleValueEvent(eventListener);
                } else if (v.getId() == R.id.addPhoto) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Выберите картинку"), 123);
                }

                if (v.getId() == R.id.back) {
                    //при переходе на другой activity анимация
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                if (v.getId() == R.id.card) {
                    //при нажатии на cardView constraintLayout котрый содержит в себе описание задания
                    if (cl.getVisibility() == View.GONE) {
                        //становиться видимым
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        cl.setVisibility(View.VISIBLE);
                        classTextInChats.setText(classTextTitle + " " + getResources().getString(R.string.grade_up));
                    } else {
                        //становиться невидимым
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        cl.setVisibility(View.GONE);
                        classTextInChats.setText(classTextTitle + " " + getResources().getString(R.string.grade_down));
                    }
                }
                if (v.getId() == R.id.btnFinishInChat) {
                    FinishTask finishTask = new FinishTask();
                    String key = "";
                    ValueEventListener valFinishTask = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            finishTask.setUID1("");
                            finishTask.setUID2("");
                            finishTask.setIdOfTask(justId);
                            String uidForCompare = mAuth.getCurrentUser().getUid();
                            finishTask.setUidForCompare(uidForCompare);

                            if (finishTask.getIdOfTask().equals(justId)) {
                                String finalKey = key;

                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String id1 = ds.child("id1").getValue(String.class);
                                    String id2 = ds.child("id2").getValue(String.class);
                                    String uid1 = ds.child("uid1").getValue(String.class);
                                    String uid2 = ds.child("uid2").getValue(String.class);
                                    finalKey = ds.getKey();
                                    String idOfTask = ds.child("idOfTask").getValue(String.class);

                                    if (idOfTask.equals(justId)) {
                                        if (uid1.equals("")) {
                                            FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    message = new AwesomeMessage();
                                                    message.setSender(mAuth.getCurrentUser().getUid());
                                                    message.setIdOfTask(justId);
                                                    message.setText("Я предлагаю закончить задание\uD83D\uDC4F");
                                                    if (mAuth.getCurrentUser().getUid().equals(id)) {
                                                        message.setRecipient(anotherId);
                                                    } else if (mAuth.getCurrentUser().getUid().equals(anotherId)) {
                                                        message.setRecipient(id);
                                                    }
                                                    String name = snapshot.child("name").getValue(String.class);

                                                    dateFprmat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                                    timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                                                    LocalDate localDate = LocalDate.now();
                                                    LocalTime localTime = LocalTime.now();

                                                    message.setDate(dateFprmat.format(localDate));
                                                    message.setTime(timeFormat.format(localTime));
                                                    message.setName(name);
                                                    FirebaseDatabase.getInstance().getReference("YourMessages").push().setValue(message);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            finishTask.setUID1(mAuth.getCurrentUser().getUid());
                                            finishTask.setId2("false");
                                            finishTask.setId1("true");
                                            FirebaseDatabase.getInstance().getReference("FinishTask").child(finalKey).child("uid1").setValue(finishTask.getUID1());
                                            FirebaseDatabase.getInstance().getReference("FinishTask").child(finalKey).child("id1").setValue(finishTask.getId1());
                                            FirebaseDatabase.getInstance().getReference("FinishTask").child(finalKey).child("idOfTask").setValue(finishTask.getIdOfTask());
                                        }
                                        if (id1.equals("true") && id2.equals("false")) {
                                            if (uid2.equals("") && !uid1.equals("") && id1.equals("true") && id2.equals("false")) {
                                                finishTask.setUID2(mAuth.getCurrentUser().getUid());
                                                if (!uid1.equals(finishTask.getUID2())) {
                                                    finishTask.setId2("true");
                                                    finishTask.setId1("true");
                                                    FirebaseDatabase.getInstance().getReference("FinishTask").child(finalKey).child("uid2").setValue(finishTask.getUID2());
                                                    FirebaseDatabase.getInstance().getReference("FinishTask").child(finalKey).child("id2").setValue(finishTask.getId2());
                                                    if (finishTask.getId1().equals("true") && finishTask.getId2().equals("true")) {

                                                        //delete messages
                                                        FirebaseDatabase.getInstance().getReference("YourMessages").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String key = "";
                                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                                    String sender = ds.child("sender").getValue(String.class);
                                                                    String recipient = ds.child("recipient").getValue(String.class);
                                                                    String idOfTask = ds.child("idOfTask").getValue(String.class);
                                                                    key = ds.getKey();
                                                                    if (recipient.equals(mAuth.getCurrentUser().getUid()) || sender.equals(mAuth.getCurrentUser().getUid())) {
                                                                        if (idOfTask.equals(justId)) {
                                                                            FirebaseDatabase.getInstance().getReference("YourMessages").child(key).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        FirebaseDatabase.getInstance().getReference("FinishTask").child(finalKey).removeValue();


                                                        if(id_str.equals(null)){
                                                            FirebaseDatabase.getInstance().getReference("FriendsInChats").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for(DataSnapshot ds : snapshot.getChildren()){

                                                                        String userId = ds.child("userId").getValue(String.class);
                                                                        String anotherId = ds.child("anotherId").getValue(String.class);

                                                                        if(mAuth.getCurrentUser().getUid().equals(userId) || mAuth.getCurrentUser().getUid().equals(anotherId)){
                                                                            String id = ds.child("id").getValue(String.class);
                                                                            FirebaseDatabase.getInstance().getReference("FriendsInChats").child(id).removeValue();
                                                                        }

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }else{
                                                            FirebaseDatabase.getInstance().getReference("FriendsInChats").child(id_str).removeValue();

                                                        }



                                                        //delete token
                                                        FirebaseDatabase.getInstance().getReference("Tokens").child(getIntent().getStringExtra("anotherId")).removeValue();
                                                        FirebaseDatabase.getInstance().getReference("Tokens").child(id).removeValue();

                                                        Users users = new Users();
                                                        users.setPoints(priceTitle);

                                                        ValueEventListener valUser = new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String points = snapshot.child("points").getValue(String.class);
                                                                String howMuchTasksDone = snapshot.child("howMuchTasksDone").getValue(String.class);
                                                                int getPoints = Integer.parseInt(users.getPoints());
                                                                int getHowMuchTasksDone = Integer.parseInt(howMuchTasksDone);
                                                                int getPointsUser = Integer.parseInt(points);
                                                                int result = getPoints + getPointsUser;

                                                                FirebaseDatabase.getInstance().getReference("User")
                                                                        .child(anotherId).child("points")
                                                                        .setValue(result + "");

                                                                FirebaseDatabase.getInstance().getReference("User")
                                                                        .child(anotherId).child("howMuchTasksDone")
                                                                        .setValue(getHowMuchTasksDone + 1 + "");

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        };

                                                        FirebaseDatabase.getInstance().getReference("User").child(anotherId).addListenerForSingleValueEvent(valUser);

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("FinishTask").addListenerForSingleValueEvent(valFinishTask);


                    Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                    intent.putExtra("email", anotherId);
                    intent.putExtra("myEmail", id);
                    startActivity(intent);

                } else if (v.getId() == R.id.callToSm) {
                    makePhoneCall();
                }

            }
        };
        sendBtn.setOnClickListener(BTNs);
        addPhotoBtn.setOnClickListener(BTNs);
        back.setOnClickListener(BTNs);
        cardView.setOnClickListener(BTNs);
        btnFinish.setOnClickListener(BTNs);
        callImage.setOnClickListener(BTNs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("MessagePhotos");
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedItem = data.getData();
            StorageReference imageReference = storageReference.child(selectedItem.getLastPathSegment());
            UploadTask uploadTask = imageReference.putFile(selectedItem);

            uploadTask = imageReference.putFile(selectedItem);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        message = new AwesomeMessage();
                        message.setIdOfTask(justId);
                        if (message.getIdOfTask().equals(justId)) {
                            message.setImgUrl(downloadUri.toString());
                        }

                        FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("name").getValue(String.class);
                                message.setName(name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        message.setSender(mAuth.getCurrentUser().getUid());
                        message.setRecipient(id);
                        if (mAuth.getCurrentUser().getUid().equals(id)) {
                            message.setRecipient(anotherId);
                        } else {
                            message.setRecipient(id);
                        }
                        dr.push().setValue(message);
                    }
                }
            });

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
        }
    }

    private void makePhoneCall() {
        //переход на activity звонка
        if (ContextCompat.checkSelfPermission(ChatActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChatActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            String dial = "tel:" + phoneTitle;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void UpdateToken() {
        //обновление token'а
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(mAuth.getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        //отправка уведомления
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ChatActivity.this, "notification was crashed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private void createNotificationChannel() {
        //создание какнала для уведолений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "IT-HAKATON",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}