package com.example.studytogetherproject.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studytogetherproject.Chat.ImageActivity;
import com.example.studytogetherproject.CommentsAndDetails.DetailActivity;
import com.example.studytogetherproject.Moduls.Friends;
import com.example.studytogetherproject.Moduls.Item;
import com.example.studytogetherproject.R;
import com.example.studytogetherproject.notificationPack.APIService;
import com.example.studytogetherproject.notificationPack.Client;
import com.example.studytogetherproject.notificationPack.Data;
import com.example.studytogetherproject.notificationPack.MyResponse;
import com.example.studytogetherproject.notificationPack.NotificationSender;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Item> arrayList = new ArrayList<>();
    private Context context;
    public static final String CHANNEL_ID = "school";
    private APIService apiService;


    public Adapter(Context context, ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Item item = arrayList.get(position);
        //holder.name.setText(item.getName());
        holder.subject.setText(item.getSubject());
        holder.points.setText(item.getPoints());
        holder.describe.setText(item.getDescribe());
        holder.nameOfTask.setText(item.getNameOfTask());
        holder.dateToFinish.setText(item.getDateToFinish());
        holder.classText.setText(item.getClassText());
        if (item.getImgUri() != null) {
            Picasso.get().load(item.getImgUri()).into(holder.imgUri);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUri, imageGoToProfile;
        TextView classText, subject, describe, points, dateToFinish, nameOfTask;
        Button begin, checkAllTask;
        CardView cardView;
        ConstraintLayout cl, cl_all;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageGoToProfile = itemView.findViewById(R.id.goToProfile);
            imgUri = itemView.findViewById(R.id.imgItem);
            classText = itemView.findViewById(R.id.classText);
            cardView = itemView.findViewById(R.id.cardView);
            cl = itemView.findViewById(R.id.cl);
            cl_all = itemView.findViewById(R.id.cl_all);
            checkAllTask = itemView.findViewById(R.id.checkAllTask);
            dateToFinish = itemView.findViewById(R.id.dateForFinish);
            nameOfTask = itemView.findViewById(R.id.nameOfTask);

            subject = itemView.findViewById(R.id.subject);
            points = itemView.findViewById(R.id.points);
            describe = itemView.findViewById(R.id.describe);
            begin = itemView.findViewById(R.id.beginToTask);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            ValueEventListener val = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String dateToFinish_string = ds.child("dateToFinish").getValue(String.class);
                        String idOfTask = ds.child("idOfTask").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);
                        String price = ds.child("points").getValue(String.class);
                        String currentDate = sdf.format(new Date());
                        try {
                            Date currentDate_date = sdf.parse(currentDate);
                            Date dateToFinish_date = sdf.parse(dateToFinish_string);
                            if (currentDate_date.after(dateToFinish_date)) {
                                Item item = arrayList.get(getAdapterPosition());
                                if (item.getIdOfTask().equals(idOfTask)) {
                                    ValueEventListener userVal = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String points = snapshot.child("points").getValue(String.class);

                                            int price_convented_to_int = Integer.parseInt(price);
                                            int points_convented_to_int = Integer.parseInt(points);
                                            int result = price_convented_to_int + points_convented_to_int;
                                            FirebaseDatabase.getInstance().getReference("User").child(id).child("points").setValue(String.valueOf(result));

                                            FirebaseDatabase.getInstance().getReference("Task").child(item.getIdOfTask()).removeValue();

                                            arrayList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), arrayList.size());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    FirebaseDatabase.getInstance().getReference("User").child(id).addListenerForSingleValueEvent(userVal);

                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            FirebaseDatabase.getInstance().getReference("Task").addListenerForSingleValueEvent(val);

            imgUri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = arrayList.get(position);
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtra("image_id", item.getImgUri());
                    context.startActivity(intent);
                }
            });

            imageGoToProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item parseItem = arrayList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("title", parseItem.getName());
                    intent.putExtra("id", parseItem.getId());
                    intent.putExtra("image", parseItem.getImgUri());
                    intent.putExtra("email", parseItem.getEmail());
                    intent.putExtra("points", parseItem.getPoints());
                    intent.putExtra("subject", parseItem.getSubject());
                    intent.putExtra("detailUrl", parseItem.getImgUri());
                    intent.putExtra("describe", parseItem.getDescribeOfUser());
                    intent.putExtra("subjectToDetail", parseItem.getSubjectOfUser());
                    intent.putExtra("imgUri", parseItem.getImgUri());
                    context.startActivity(intent);
                }
            });

            checkAllTask.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (cl.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        cl.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        cl.setVisibility(View.GONE);

                    }
                }
            });

            begin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                    goToChat();
                    Snackbar snackbar = Snackbar.make(v, "Ваша заявка была отправлена!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();

                }
            });

        }


        public void sendNotifications(String usertoken, String title, String message) {
            Data data = new Data(title, message);
            NotificationSender sender = new NotificationSender(data, usertoken);
            apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    if (response.code() == 200) {
                        if (response.body().success != 1) {
                            Toast.makeText(context, "NOOOOOOOOOOOOOO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MyResponse> call, Throwable t) {

                }
            });
        }

        private void goToChat() {
            int position = getAdapterPosition();
            Item parseItem = arrayList.get(position);
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Friends");

            Friends friends = new Friends();
            friends.setName("name");
            friends.setNameAnotherUser(parseItem.getName());
            friends.setIdOfTask(parseItem.getIdOfTask());
            friends.setSubjectOfUser("subject");
            friends.setDescribtionOfUser("describtionOfUser");
            friends.setEmail(parseItem.getEmail());
            friends.setNameOfTask(parseItem.getNameOfTask());
            friends.setPoints(parseItem.getPoints());
            friends.setSubject(parseItem.getSubject());
            friends.setPhone(parseItem.getPhone());
            friends.setClassText(parseItem.getClassText());
            friends.setDescribe(parseItem.getDescribe());
            friends.setImg(parseItem.getImgUri());
            friends.setImgUri1(parseItem.getImgUri1());
            friends.setImgUri2(parseItem.getImgUri2());
            friends.setDateToFinish(parseItem.getDateToFinish());
            friends.setMyEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            ValueEventListener valTask = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.child("id").getValue(String.class);
                        friends.setUserId(id);
                        int counter = 0;
                        counter += 1;

                        FirebaseDatabase.getInstance().getReference("User").child(id).child("howMuchNotifications").setValue(String.valueOf(counter));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            FirebaseDatabase.getInstance().getReference("Task").addListenerForSingleValueEvent(valTask);


            ValueEventListener val = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String s = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        s = ds.getKey();
                        friends.setId(s);
                    }
                    dr.child(s).child("id").setValue(friends.getId());

                    String finalS = s;

                    ValueEventListener valUsers = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String sUser = "";
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                sUser = ds.getKey();

                                ValueEventListener val = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("name").getValue(String.class);
                                        String subject = snapshot.child("subject").getValue(String.class);
                                        String describtionOfUser = snapshot.child("describtion").getValue(String.class);
                                        String imgUri2 = snapshot.child("imgUri").getValue(String.class);
                                        friends.setName(name);
                                        friends.setImgUri2(imgUri2);
                                        friends.setSubjectOfUser(subject);
                                        friends.setDescribtionOfUser(describtionOfUser);
                                        dr.child(finalS).child("name").setValue(friends.getName());
                                        dr.child(finalS).child("imgUri2").setValue(friends.getImgUri2());
                                        dr.child(finalS).child("imgUri1").setValue(friends.getImgUri1());
                                        dr.child(finalS).child("describtionOfUser").setValue(friends.getDescribtionOfUser());
                                        dr.child(finalS).child("subjectOfUser").setValue(friends.getSubjectOfUser());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(val);

                            }


                            //тут нотификация
                            friends.setAnotherId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            dr.child(finalS).child("userId").setValue(friends.getUserId());
                            dr.child(finalS).child("anotherId").setValue(friends.getAnotherId());

                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(friends.getUserId()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String name = snapshot.child("name").getValue(String.class);
                                            String usertoken = dataSnapshot.getValue(String.class);
                                            sendNotifications(usertoken, "Заявка на выполнение!",  name + " хочет выполнить задание");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(valUsers);

                    dr.child(finalS).child("name").setValue(friends.getName());
                    dr.child(finalS).child("imgUri1").setValue(friends.getImgUri1());
                    dr.child(finalS).child("imgUri2").setValue(friends.getImgUri2());
                    dr.child(finalS).child("idOfTask").setValue(friends.getIdOfTask());
                    dr.child(finalS).child("email").setValue(friends.getEmail());
                    dr.child(finalS).child("nameOfTask").setValue(friends.getNameOfTask());
                    dr.child(finalS).child("points").setValue(friends.getPoints());
                    dr.child(finalS).child("phone").setValue(friends.getPhone());
                    dr.child(finalS).child("classText").setValue(friends.getClassText());
                    dr.child(finalS).child("describe").setValue(friends.getDescribe());
                    dr.child(finalS).child("img").setValue(friends.getImg());
                    dr.child(finalS).child("dateToFinish").setValue(friends.getDateToFinish());
                    dr.child(finalS).child("myEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            dr.push().setValue(friends);
            FirebaseDatabase.getInstance().getReference("Friends").addListenerForSingleValueEvent(val);


            //  context.startActivity(intent);
        }

    }

    public void filterList(ArrayList<Item> filterList) {
        arrayList = filterList;
        notifyDataSetChanged();
    }

}
