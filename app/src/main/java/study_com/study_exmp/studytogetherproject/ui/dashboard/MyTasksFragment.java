package study_com.study_exmp.studytogetherproject.ui.dashboard;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import study_com.study_exmp.studytogetherproject.Moduls.Item;
import study_com.study_exmp.studytogetherproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyTasksFragment extends Fragment {
    private RecyclerView rv;
    private AdapterForMyTasks adapter;
    private ArrayList<Item> arrayList = new ArrayList<>();
    private TextView textMainAct;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);

        init(view);
        recyclerBuilder();
        readMyTasks();

        return view;
    }
    private void init(View view) {
        rv = view.findViewById(R.id.rv);
        textMainAct = view.findViewById(R.id.textMainAct);
        textMainAct.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
    }



    private void recyclerBuilder() {
        rv.setHasFixedSize(true);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setReverseLayout(true);
        lin.setStackFromEnd(true);
        rv.setLayoutManager(lin);
    }

    private void readMyTasks() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ValueEventListener valueEventTask = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String subjectTask = ds.child("subject").getValue(String.class);
                            String id = ds.child("id").getValue(String.class);
                            String describtion = ds.child("describe").getValue(String.class);
                            String nameTask = ds.child("name").getValue(String.class);
                            String emailTask = ds.child("email").getValue(String.class);
                            String imgPath = ds.child("img").getValue(String.class);
                            String points = ds.child("points").getValue(String.class);
                            String phone = ds.child("phone").getValue(String.class);
                            String nameOfTask = ds.child("nameOfTask").getValue(String.class);
                            String dateToFinish = ds.child("dateToFinish").getValue(String.class);
                            String classText = ds.child("classText").getValue(String.class);
                            String subjectToDetail = ds.child("subjectOfUser").getValue(String.class);
                            String describeToDetail = ds.child("describtionOfUser").getValue(String.class);
                            String idOfTask = ds.child("idOfTask").getValue(String.class);
                            String imgUri1 = ds.child("imgUri1").getValue(String.class);

                            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(emailTask)){
                                if (subjectTask != null) {
                                    arrayList.add(new Item(nameTask, imgPath,
                                            subjectTask, points,
                                            describtion, emailTask, phone, nameOfTask, dateToFinish, classText, subjectToDetail, describeToDetail, id, idOfTask, imgUri1));

                                    adapter = new AdapterForMyTasks(getContext(), arrayList);
                                    rv.setAdapter(adapter);
                                    rv.setVisibility(View.VISIBLE);
                                    textMainAct.setVisibility(View.GONE);
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                FirebaseDatabase.getInstance().getReference("Task").addListenerForSingleValueEvent(valueEventTask);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        uidRef.addListenerForSingleValueEvent(eventListenerUser);
    }
}