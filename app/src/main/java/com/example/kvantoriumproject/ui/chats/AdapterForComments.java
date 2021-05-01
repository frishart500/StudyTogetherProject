package com.example.kvantoriumproject.ui.chats;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.ChooseInf;
import com.example.kvantoriumproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterForComments extends RecyclerView.Adapter<AdapterForComments.ViewHolder> {
    private ArrayList<ChooseInf> arrayList = new ArrayList<>();
    private Context context;

    public AdapterForComments(Context context, ArrayList<ChooseInf> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterForComments.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raiting, parent, false);
        return new AdapterForComments.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForComments.ViewHolder holder, int position) {
        ChooseInf item = arrayList.get(position);
        holder.name.setText(item.getName());
        holder.comment.setText(item.getComment());

        ValueEventListener val = new ValueEventListener() {
            double count = 0.0;
            double points = 0.0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);
                    String ratingPoints = ds.child("raiting").getValue(String.class);
                    Double val = Double.parseDouble(ratingPoints);



                    count += Double.parseDouble(ratingPoints);

                }


                ChooseInf chooseInf = new ChooseInf();
                Double val = Double.parseDouble(item.getRaiting());
                if (val < 1 && val > 0) {
                    holder.stars.setImageResource(R.drawable.zero_five);
                } else if (val <= 1.5 && val > 0 && val > 1) {
                    holder.stars.setImageResource(R.drawable.one_five);
                } else if (val > 0.5 && val > 0 && val < 1.5) {
                    holder.stars.setImageResource(R.drawable.one);
                } else if (val > 1.5 && val > 0 && val < 2.5) {
                    holder.stars.setImageResource(R.drawable.two);
                } else if (val >= 2 && val > 0 && val <= 2.5) {
                    holder.stars.setImageResource(R.drawable.two_five);
                } else if (val > 2.5 && val > 0 && val <= 3) {
                    holder.stars.setImageResource(R.drawable.three);
                } else if (val > 3 && val > 0 && val <= 3.5) {
                    holder.stars.setImageResource(R.drawable.three_five);
                } else if (val > 3.5 && val > 0 && val <= 4) {
                    holder.stars.setImageResource(R.drawable.four);
                } else if (val > 4 && val > 0 && val <= 4.5) {
                    holder.stars.setImageResource(R.drawable.four_five);
                } else if (val > 4.5 && val > 0 && val <= 5) {
                    holder.stars.setImageResource(R.drawable.five);
                } else if (val == 0) {
                    holder.stars.setImageResource(R.drawable.zero);
                }
                chooseInf.setRatingMiddle(String.valueOf(points));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("Raiting").addListenerForSingleValueEvent(val);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, comment;
        private ImageView stars;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            stars = itemView.findViewById(R.id.stars);
        }

    }


}