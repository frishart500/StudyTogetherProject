package com.example.kvantoriumproject.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.kvantoriumproject.R;
import java.util.List;

public class AwesomeMessageAdapter extends ArrayAdapter<AwesomeMessage> {
    public AwesomeMessageAdapter(Context context, int resource, List<AwesomeMessage> messages) {
        super(context, resource, messages);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);
        }
        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        TextView text = convertView.findViewById(R.id.text);
        TextView name = convertView.findViewById(R.id.name);



        AwesomeMessage message = getItem(position);
        boolean isText = message.getImgUrl() == null;

        if(isText){
            text.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            text.setText(message.getText());
        }else{
            text.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext()).load(message.getImgUrl()).into(photoImageView);
        }
        name.setText(message.getName());
        return convertView;
    }
}
