package com.example.kvantoriumproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvantoriumproject.Items.FinishTask;
import com.example.kvantoriumproject.MainClasses.MainActivity;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.Items.User;
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

import java.util.ArrayList;
import java.util.List;

import com.example.kvantoriumproject.notificationPack.APIService;
import com.example.kvantoriumproject.notificationPack.Client;
import com.example.kvantoriumproject.notificationPack.Data;
import com.example.kvantoriumproject.notificationPack.MyResponse;
import com.example.kvantoriumproject.notificationPack.NotificationSender;
import com.example.kvantoriumproject.notificationPack.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "school";
    private ListView messageListView;
    private AwesomeMessageAdapter adapter;
    private Button sendBtn, addPhotoBtn, btnFinish;
    private EditText messageEditText;
    private DatabaseReference dr;
    private String nameTitle, phoneTitle, classTextTitle, describeTitle, priceTitle, dateToFinishTitle, emailTitle, nameOfTaskTitle, emailMine, id_str;
    private String recipientUserId, id, id_from_chats, justId;
    private ChildEventListener childEventListener;
    private ImageView back, callImage;
    private TextView nameInChats, classTextInChats, describe, price, dateToFinish;
    private ConstraintLayout cl;
    private CardView cardView;


    boolean notify = false;
    private APIService apiService;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        nameTitle = getIntent().getStringExtra("name");
        describeTitle = getIntent().getStringExtra("describeTask");
        classTextTitle = getIntent().getStringExtra("classText");
        recipientUserId = getIntent().getStringExtra("email");
        phoneTitle = getIntent().getStringExtra("phone");
        priceTitle = getIntent().getStringExtra("price");
        id = getIntent().getStringExtra("userId");
        id_from_chats = getIntent().getStringExtra("id");
        dateToFinishTitle = getIntent().getStringExtra("dateToFinish");
        nameOfTaskTitle = getIntent().getStringExtra("nameOfTask");
        justId = getIntent().getStringExtra("justId");
        id_str = getIntent().getStringExtra("id");

        init();

        describe.setText(describeTitle);
        price.setText(priceTitle);
        dateToFinish.setText(dateToFinishTitle);
        nameInChats.setText(nameTitle);

        getSupportActionBar().hide();
        List<AwesomeMessage> awesomeMessages = new ArrayList<>();
        adapter = new AwesomeMessageAdapter(this, R.layout.message_item, awesomeMessages);
        messageListView.setAdapter(adapter);
        classTextInChats.setText(classTextTitle + " класс ↓");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {


                AwesomeMessage message = dataSnapshot.getValue(AwesomeMessage.class);
                nameTitle = getIntent().getStringExtra("name");
                if (message.getIdOfTask().equals(justId)) {
                    if (message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            && message.getRecipient().equals(id) || message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                            message.getRecipient().equals(getIntent().getStringExtra("anotherId"))) {
                        message.setMine(true);
                        adapter.add(message);

                    }
                    if (message.getRecipient().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            && message.getSender().equals(id) || message.getRecipient().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                            message.getSender().equals(getIntent().getStringExtra("anotherId"))) {
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

        onClick();
        UpdateToken();
        createNotificationChannel();
    }



    private void status(String status) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        User user = new User();
        user.setStatus(status);
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


    private void init() {

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
        dr = FirebaseDatabase.getInstance().getReference("YourMessages");
    }

    private void onClick() {

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendBtn.setEnabled(true);
                } else {
                    sendBtn.setEnabled(false);
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

                    notify = true;

                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

                    ValueEventListener eventListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue(String.class);

                            System.out.println(name);
                            AwesomeMessage message = new AwesomeMessage();
                            message.setText(messageEditText.getText().toString().trim());
                            message.setName(name);
                            message.setSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            message.setIdOfTask(justId);
                            message.setRecipient(id);


                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(id)) {
                                String anotherId = getIntent().getStringExtra("anotherId");
                                message.setRecipient(anotherId);

                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(anotherId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        ValueEventListener valStatus = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String status = snapshot.child("status").getValue(String.class);
                                                if(status.equals("offline")){
                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                    sendNotifications(usertoken, name, message.getText());
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        };FirebaseDatabase.getInstance().getReference().child("User").child(anotherId).addListenerForSingleValueEvent(valStatus);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else if (getIntent().getStringExtra("anotherId").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                message.setRecipient(id);

                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(id).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        ValueEventListener valStatus = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String status = snapshot.child("status").getValue(String.class);
                                                if(status.equals("offline")){
                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                    sendNotifications(usertoken, name, message.getText());
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        };FirebaseDatabase.getInstance().getReference().child("User").child(id).addListenerForSingleValueEvent(valStatus);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            dr.push().setValue(message)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });

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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                if (v.getId() == R.id.card) {
                    if (cl.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        cl.setVisibility(View.VISIBLE);
                        classTextInChats.setText(classTextTitle + " класс ↑");
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        cl.setVisibility(View.GONE);
                        classTextInChats.setText(classTextTitle + " класс ↓");
                    }
                }
                if (v.getId() == R.id.btnFinishInChat) {
                    FinishTask finishTask = new FinishTask();
                    String s = "";
                    ValueEventListener valFinishTask = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            finishTask.setUID1("");
                            finishTask.setUID2("");
                            finishTask.setIdOfTask(justId);
                            String uidForCompare = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            finishTask.setUidForCompare(uidForCompare);

                            if (finishTask.getIdOfTask().equals(justId)) {
                                String s1 = s;

                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String id1 = ds.child("id1").getValue(String.class);
                                    String id2 = ds.child("id2").getValue(String.class);
                                    String uid1 = ds.child("uid1").getValue(String.class);
                                    String uid2 = ds.child("uid2").getValue(String.class);
                                    s1 = ds.getKey();
                                    String idOfTask = ds.child("idOfTask").getValue(String.class);
                                    System.out.println(uidForCompare + " UID");
                                    System.out.println(finishTask.getUID1() + " UID1");
                                    System.out.println(finishTask.getUID2() + " UID2");
                                    if (idOfTask.equals(justId)) {
                                        if (uid1.equals("")) {
                                            finishTask.setUID1(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            finishTask.setId2("false");
                                            finishTask.setId1("true");
                                            FirebaseDatabase.getInstance().getReference("FinishTask").child(s1).child("uid1").setValue(finishTask.getUID1());
                                            FirebaseDatabase.getInstance().getReference("FinishTask").child(s1).child("id1").setValue(finishTask.getId1());
                                            FirebaseDatabase.getInstance().getReference("FinishTask").child(s1).child("idOfTask").setValue(finishTask.getIdOfTask());
                                        }
                                        if (id1.equals("true") && id2.equals("false")) {
                                            if (uid2.equals("") && !uid1.equals("") && id1.equals("true") && id2.equals("false")) {
                                                finishTask.setUID2(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                if (!uid1.equals(finishTask.getUID2())) {
                                                    finishTask.setId2("true");
                                                    finishTask.setId1("true");
                                                    FirebaseDatabase.getInstance().getReference("FinishTask").child(s1).child("uid2").setValue(finishTask.getUID2());
                                                    FirebaseDatabase.getInstance().getReference("FinishTask").child(s1).child("id2").setValue(finishTask.getId2());
                                                    if (finishTask.getId1().equals("true") && finishTask.getId2().equals("true")) {

                                                        //delete messages
                                                        FirebaseDatabase.getInstance().getReference("YourMessages").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String key = "";
                                                                for(DataSnapshot ds : snapshot.getChildren()){
                                                                    String sender = ds.child("sender").getValue(String.class);
                                                                    String recipient = ds.child("recipient").getValue(String.class);
                                                                    String idOfTask = ds.child("idOfTask").getValue(String.class);
                                                                    key = ds.getKey();
                                                                    if(recipient.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || sender.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                                        if(idOfTask.equals(justId)){
                                                                            FirebaseDatabase.getInstance().getReference("YourMessages").child(key).removeValue();
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        FirebaseDatabase.getInstance().getReference("FinishTask").child(s1).removeValue();
                                                        FirebaseDatabase.getInstance().getReference("FriendsInChats").child(id_str).removeValue();
                                                        //delete token
                                                        FirebaseDatabase.getInstance().getReference("Tokens").child(getIntent().getStringExtra("anotherId")).removeValue();
                                                        FirebaseDatabase.getInstance().getReference("Tokens").child(id).removeValue();

                                                        User user = new User();
                                                        user.setPoints(priceTitle);

                                                        ValueEventListener valUser = new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String points = snapshot.child("points").getValue(String.class);
                                                                String howMuchTasksDone = snapshot.child("howMuchTasksDone").getValue(String.class);
                                                                int getPoints = Integer.parseInt(user.getPoints());
                                                                int getHowMuchTasksDone = Integer.parseInt(howMuchTasksDone);
                                                                int getPointsUser = Integer.parseInt(points);
                                                                int result = getPoints + getPointsUser;

                                                                FirebaseDatabase.getInstance().getReference("User")
                                                                        .child(getIntent().getStringExtra("anotherId")).child("points")
                                                                        .setValue(result + "");

                                                                FirebaseDatabase.getInstance().getReference("User")
                                                                        .child(getIntent().getStringExtra("anotherId")).child("howMuchTasksDone")
                                                                        .setValue(getHowMuchTasksDone + 1 + "");

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        };

                                                        FirebaseDatabase.getInstance().getReference("User").child(getIntent().getStringExtra("anotherId")).addListenerForSingleValueEvent(valUser);

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    System.out.println(s1 + " print id");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("FinishTask").addListenerForSingleValueEvent(valFinishTask);


                    Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                    intent.putExtra("email", getIntent().getStringExtra("anotherId"));
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

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        AwesomeMessage message = new AwesomeMessage();
                        message.setIdOfTask(justId);
                        if (message.getIdOfTask().equals(justId)) {
                            message.setImgUrl(downloadUri.toString());

                        }

                        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("name").getValue(String.class);
                                message.setName(name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        message.setSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        message.setRecipient(id);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(id)) {
                            String anotherId = getIntent().getStringExtra("anotherId");
                            message.setRecipient(anotherId);
                        } else {
                            message.setRecipient(id);
                        }
                        System.out.println(message.getImgUrl());
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
            } else {

            }
        }
    }

    private void makePhoneCall() {


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
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ChatActivity.this, "NOOOOOOOOOOOOOO", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private void createNotificationChannel() {
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