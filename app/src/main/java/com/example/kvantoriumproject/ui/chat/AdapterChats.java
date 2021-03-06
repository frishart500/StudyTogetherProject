package com.example.kvantoriumproject.ui.chat;

import android.content.Context;
import android.content.Intent;
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
import com.example.kvantoriumproject.ui.dashboard.Adapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterChats extends RecyclerView.Adapter<AdapterChats.ViewHolder>{
    private ArrayList<ItemChat> arrayList = new ArrayList<>();
    private Context context;
    public AdapterChats(Context context, ArrayList<ItemChat> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterChats.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new AdapterChats.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChats.ViewHolder holder, int position) {
        ItemChat item = arrayList.get(position);
        holder.name.setText(item.getName());
        holder.subject.setText(item.getSubject());
        holder.discribtionOfTaskInChats.setText(item.getDesc());
        Picasso.get().load(item.getImg()).into(holder.img);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, email, discribtionOfTaskInChats, subject;
        private ImageView img;
        private Button btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            discribtionOfTaskInChats = itemView.findViewById(R.id.describeOfTaskInChat);
            subject = itemView.findViewById(R.id.subject);
            name = itemView.findViewById(R.id.nameText);
            img = itemView.findViewById(R.id.imgItem);
            btn = itemView.findViewById(R.id.beginToTask);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToChat();
                }
            });
        }

        private void goToChat(){
            int position = getAdapterPosition();
            ItemChat parseItem = arrayList.get(position);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name", parseItem.getName());
            intent.putExtra("email", parseItem.getEmail());
            context.startActivity(intent);
        }
    }

}
