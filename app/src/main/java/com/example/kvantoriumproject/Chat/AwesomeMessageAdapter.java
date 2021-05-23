package com.example.kvantoriumproject.Chat;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.kvantoriumproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AwesomeMessageAdapter extends ArrayAdapter<AwesomeMessage> {
    private List<AwesomeMessage> messages;
    private Activity activity;

    public AwesomeMessageAdapter(Activity context, int resource, List<AwesomeMessage> messages) {
        super(context, resource, messages);
        this.messages = messages;
        this.activity = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        AwesomeMessage awesomeMessage = getItem(position);
        AwesomeMessage chat = messages.get(position);
        int layoutResources = 0;
        int viewType = getItemViewType(position);

        if (viewType == 0) {
            layoutResources = R.layout.mine_message;
        } else {
            layoutResources = R.layout.your_message;
        }

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(layoutResources, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        boolean isText = awesomeMessage.getImgUrl() == null;

        if (isText) {
            viewHolder.messageTextView.setVisibility(View.VISIBLE);
            viewHolder.nameOfUser.setVisibility(View.VISIBLE);
            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.VISIBLE);

            viewHolder.nameOfUser.setText(awesomeMessage.getName());
            viewHolder.date.setText(awesomeMessage.getDate());
            viewHolder.time.setText(awesomeMessage.getTime());
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.messageTextView.setText(awesomeMessage.getText());

            if(position == messages.size() - 1){
                if(chat.getSeen()){
                    viewHolder.img_seen.setVisibility(View.GONE);
                }else{
                    viewHolder.img_seen.setVisibility(View.VISIBLE);
                }
            }else{
                viewHolder.img_seen.setVisibility(View.GONE);
            }
        } else {
            viewHolder.date.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.img_seen.setVisibility(View.GONE);
            viewHolder.nameOfUser.setVisibility(View.VISIBLE);
            viewHolder.nameOfUser.setText(awesomeMessage.getName());
            viewHolder.messageTextView.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setBackgroundResource(R.drawable.background_another_user);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageActivity.class);
                    intent.putExtra("image_id", awesomeMessage.getImgUrl());
                    getContext().startActivity(intent);
                }
            });
            Glide.with(viewHolder.imageView.getContext())
                    .load(awesomeMessage.getImgUrl())
                    .into(viewHolder.imageView);
        }



        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int flag;
        AwesomeMessage awesomeMessage = messages.get(position);
        if (awesomeMessage.isMine()) {
            flag = 0;
        } else {
            flag = 1;
        }
        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class ViewHolder {
        private TextView messageTextView, nameOfUser, date, time;
        private ImageView imageView, img_seen;

        public ViewHolder(View v) {
            imageView = v.findViewById(R.id.photoOfUser);
            img_seen = v.findViewById(R.id.img_seen);
            date = v.findViewById(R.id.date);
            time = v.findViewById(R.id.time);
            messageTextView = v.findViewById(R.id.bubbleText);
            nameOfUser = v.findViewById(R.id.nameOfUser);
        }

    }

}
