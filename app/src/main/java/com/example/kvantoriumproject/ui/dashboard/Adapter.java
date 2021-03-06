package com.example.kvantoriumproject.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvantoriumproject.Chat.ChatActivity;
import com.example.kvantoriumproject.Item;
import com.example.kvantoriumproject.R;
import com.example.kvantoriumproject.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Item> arrayList = new ArrayList<>();
    private Context context;

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
        holder.name.setText(item.getName());
        holder.subject.setText(item.getSubject());
        holder.points.setText(item.getPoints());
        holder.describe.setText(item.getDescribe());
        Picasso.get().load(item.getImgUri()).into(holder.imgUri);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgUri;
        TextView name, subject, describe, points;
        Button begin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUri = itemView.findViewById(R.id.imgItem);
            name = itemView.findViewById(R.id.nameText);

            subject = itemView.findViewById(R.id.subject);
            points = itemView.findViewById(R.id.points);
            describe = itemView.findViewById(R.id.describe);
            begin = itemView.findViewById(R.id.beginToTask);

            begin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToChat();
                    arrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), arrayList.size());
                }
            });
            itemView.setOnClickListener(this);
        }

        private void goToChat() {
            int position = getAdapterPosition();
            Item parseItem = arrayList.get(position);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name", parseItem.getName());
            intent.putExtra("email", parseItem.getEmail());
            intent.putExtra("phone", parseItem.getPhone());
            intent.putExtra("img", parseItem.getImgUri());
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Item parseItem = arrayList.get(position);
            Intent intent = new Intent(context, DeteilActivity.class);
            intent.putExtra("title", parseItem.getName());
            intent.putExtra("image", parseItem.getImgUri());
            //intent.putExtra("detailUrl", parseItem.get());
            context.startActivity(intent);
        }
    }


}
