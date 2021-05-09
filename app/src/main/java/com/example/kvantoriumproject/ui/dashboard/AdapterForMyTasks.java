package com.example.kvantoriumproject.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.Chat.ImageActivity;
import com.example.kvantoriumproject.Moduls.Item;
import com.example.kvantoriumproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterForMyTasks extends RecyclerView.Adapter<AdapterForMyTasks.ViewHolder> {

    private ArrayList<Item> arrayList = new ArrayList<>();
    private Context context;

    public AdapterForMyTasks(Context context, ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterForMyTasks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_my_tasks, parent, false);
        return new AdapterForMyTasks.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        TextView classText, subject, describe, points, dateToFinish, nameOfTask;
        Button begin, checkAllTask;
        CardView cardView;
        ImageView deleteTask, imgUri;
        ConstraintLayout cl, cl_all;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUri = itemView.findViewById(R.id.imgItem);
            classText = itemView.findViewById(R.id.classTextInMyTasks);
            cardView = itemView.findViewById(R.id.cardView);
            cl = itemView.findViewById(R.id.cl);
            cl_all = itemView.findViewById(R.id.cl_all);
            checkAllTask = itemView.findViewById(R.id.checkAllTask);
            dateToFinish = itemView.findViewById(R.id.dateForFinish);
            nameOfTask = itemView.findViewById(R.id.nameOfTask);
            deleteTask = itemView.findViewById(R.id.deleteTask);

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
                        String price = ds.child("points").getValue(String.class);
                        String currentDate = sdf.format(new Date());
                        try {
                            Date currentDate_date = sdf.parse(currentDate);
                            Date dateToFinish_date = sdf.parse(dateToFinish_string);
                            if (currentDate_date.after(dateToFinish_date)) {
                                System.out.println(currentDate + " current date");
                                System.out.println(dateToFinish_date + " date to fisish date");
                                System.out.println("current date before");
                                Item item = arrayList.get(getAdapterPosition());
                                if(item.getIdOfTask().equals(idOfTask)){

                                    ValueEventListener userVal = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String points = snapshot.child("points").getValue(String.class);

                                            int price_convented_to_int = Integer.parseInt(price);
                                            int points_convented_to_int = Integer.parseInt(points);
                                            int result = price_convented_to_int + points_convented_to_int;
                                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue(String.valueOf(result));

                                            FirebaseDatabase.getInstance().getReference("Task").child(item.getIdOfTask()).removeValue();

                                            arrayList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), arrayList.size());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(userVal);



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
                    Intent intent = new Intent(context, ImageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("image_id", item.getImgUri());
                    context.startActivity(intent);
                }
            });

            deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = arrayList.get(position);
                    System.out.println(item.getIdOfTask());

                    ValueEventListener userVal = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String points = snapshot.child("points").getValue(String.class);

                            int price_convented_to_int = Integer.parseInt(item.getPoints());
                            int points_convented_to_int = Integer.parseInt(points);
                            int result = price_convented_to_int + points_convented_to_int;
                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue(String.valueOf(result));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(userVal);
                    FirebaseDatabase.getInstance().getReference("Task").child(item.getIdOfTask()).removeValue();

                    arrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), arrayList.size());

                    Snackbar snackbar = Snackbar.make(v, "Вы удалили задание! Баллы, которые вы заплатили за задание были вам возвращены.", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(0XFFffffff);
                    snackbar.setTextColor(0XFF601C80);
                    snackbar.show();
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
        }

        private void getUserInfo(){
        }

    }
}
