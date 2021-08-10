package study_com.studytogetherproject.ui.friendsInChats;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import study_com.studytogetherproject.Chat.ChatActivity;
import study_com.studytogetherproject.Moduls.ItemChat;
import study_com.study_exmp.studytogetherproject.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterChats extends RecyclerView.Adapter<AdapterChats.ViewHolder> {
    private ArrayList<ItemChat> arrayList = new ArrayList<>();
    private Context context;
    Intent intent;

    public AdapterChats(Context context, ArrayList<ItemChat> arrayList) {
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
        ValueEventListener valUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);

                ValueEventListener val = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String nameInChats = ds.child("name").getValue(String.class);
                            String nameAnotherUser = ds.child("nameAnotherUser").getValue(String.class);
                            String id = ds.child("anotherId").getValue(String.class);
                            String id1 = ds.child("userId").getValue(String.class);
                            if (id1.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                ValueEventListener valAnotherUIDStatus = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String status = snapshot.child("status").getValue(String.class);
                                        if (status.equals("online")) {
                                            holder.status.setImageResource(R.drawable.open_eye);
                                        } else if (status.equals("offline")) {
                                            holder.status.setImageResource(R.drawable.close_eye);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                FirebaseDatabase.getInstance().getReference("User").child(id).addListenerForSingleValueEvent(valAnotherUIDStatus);
                            } else if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                ValueEventListener valAnotherUIDStatus = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String status = snapshot.child("status").getValue(String.class);
                                        if (status.equals("online")) {
                                            holder.status.setImageResource(R.drawable.open_eye);
                                        } else {
                                            holder.status.setImageResource(R.drawable.close_eye);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                FirebaseDatabase.getInstance().getReference("User").child(id1).addListenerForSingleValueEvent(valAnotherUIDStatus);
                            }



                            //here fucking bug

                            if (name.equals(nameAnotherUser)) {
                                holder.name.setText(arrayList.get(position).getName() + ", ");
                                Glide.with(context).load(item.getImg1()).into(holder.img);
                                if(nameAnotherUser.equals(arrayList.get(position).getName())){
                                    holder.name.setText(arrayList.get(position).getNameAnotherPerson() + ", ");
                                    Glide.with(context).load(item.getImg2()).into(holder.img);
                                }
                            }
                            else if (nameInChats.equals(arrayList.get(position).getName())) {
                                Glide.with(context).load(item.getImg2()).into(holder.img);
                                holder.name.setText(arrayList.get(position).getNameAnotherPerson() + ", ");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                FirebaseDatabase.getInstance().getReference("FriendsInChats").addListenerForSingleValueEvent(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valUser);

        holder.classText.setText(item.getClassText());
        holder.nameOfTask.setText(item.getNameOfTask());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, classText, nameOfTask;
        private ImageView img, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfTask = itemView.findViewById(R.id.nameOfTask);
            classText = itemView.findViewById(R.id.classText);
            name = itemView.findViewById(R.id.nameText);
            img = itemView.findViewById(R.id.imgItem);
            status = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void goToChat() {
            ValueEventListener valUser = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    intent = new Intent(context, ChatActivity.class);
                    int position = getAdapterPosition();
                    intent.putExtra("email", arrayList.get(position).getEmail());
                    intent.putExtra("describeTask", arrayList.get(position).getDescribe());
                    intent.putExtra("myEmail", arrayList.get(position).getMyEmail());
                    intent.putExtra("classText", arrayList.get(position).getClassText());
                    intent.putExtra("nameOfTask", arrayList.get(position).getNameOfTask());
                    intent.putExtra("dateToFinish", arrayList.get(position).getDateToFinish());
                    intent.putExtra("phone", arrayList.get(position).getPhone());
                    intent.putExtra("price", arrayList.get(position).getPrice());
                    intent.putExtra("userId", arrayList.get(position).getUserId());
                    intent.putExtra("anotherId", arrayList.get(position).getAnotherId());
                    intent.putExtra("justId", arrayList.get(position).getJustId());
                    intent.putExtra("id", arrayList.get(position).getId());

                    String name = snapshot.child("name").getValue(String.class);
                    ValueEventListener val = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String nameInChats = ds.child("name").getValue(String.class);
                                String nameAnotherUser = ds.child("nameAnotherUser").getValue(String.class);

                                if (name.equals(nameAnotherUser)) {
                                    intent.putExtra("name", arrayList.get(position).getName());
                                    if(nameAnotherUser.equals(arrayList.get(position).getName())){
                                        intent.putExtra("name", arrayList.get(position).getNameAnotherPerson());
                                    }
                                }
                                else if (nameInChats.equals(arrayList.get(position).getName())) {
                                    intent.putExtra("name", arrayList.get(position).getNameAnotherPerson());
                                }
                            }
                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    FirebaseDatabase.getInstance().getReference("FriendsInChats").addListenerForSingleValueEvent(val);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valUser);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            goToChat();

        }
    }

    public void filterList(ArrayList<ItemChat> filterList) {
        arrayList = filterList;
        notifyDataSetChanged();
    }

}

