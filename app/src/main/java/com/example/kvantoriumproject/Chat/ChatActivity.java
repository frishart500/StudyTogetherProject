package com.example.kvantoriumproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ActivityNavigator;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.kvantoriumproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ListView messageListView;
    private ProgressBar progressBar;
    private AwesomeMessageAdapter adapter;
    private Button sendBtn, addPhotoBtn;
    private EditText messageEditText;
    private DatabaseReference dr;
    private String nameTitle;
    private String recipientUserId;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        onClick();
        nameTitle = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(nameTitle);
        List<AwesomeMessage> awesomeMessages = new ArrayList<>();
        adapter = new AwesomeMessageAdapter(this, R.layout.message_item, awesomeMessages);
        messageListView.setAdapter(adapter);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        recipientUserId = getIntent().getStringExtra("email");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                AwesomeMessage awesomeMessage = dataSnapshot.getValue(AwesomeMessage.class);
                nameTitle = getIntent().getStringExtra("name");
                System.out.println(awesomeMessage.getRecipient());
                System.out.println(awesomeMessage.getSender());
                if(awesomeMessage.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        && awesomeMessage.getRecipient().equals(recipientUserId)){
                    adapter.add(awesomeMessage);
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
    private void init(){
        messageListView = findViewById(R.id.list);
        sendBtn = findViewById(R.id.sendMessage);
        addPhotoBtn = findViewById(R.id.addPhoto);
        progressBar = findViewById(R.id.progressBar);
        messageEditText = findViewById(R.id.messageEditText);
        dr = FirebaseDatabase.getInstance().getReference("YourMessages");
    }

    private void onClick(){

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0){
                    sendBtn.setEnabled(true);
                }else{
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
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.sendMessage){

                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference uidRefGetUid = rootRef.child("User").child(uid);

                    ValueEventListener eventListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue(String.class);
                            System.out.println(name);
                            AwesomeMessage message = new AwesomeMessage();
                            message.setText(messageEditText.getText().toString());
                            message.setName(name);
                            message.setSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            message.setRecipient(recipientUserId);
                            message.setImgUrl(null);

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
                }else if(v.getId() == R.id.addPhoto){

                }
            }
        };
        sendBtn.setOnClickListener(BTNs);
        addPhotoBtn.setOnClickListener(BTNs);
    }

    private void readName() {

    }
}