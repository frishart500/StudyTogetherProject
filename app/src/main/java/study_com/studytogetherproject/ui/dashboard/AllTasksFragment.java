package study_com.studytogetherproject.ui.dashboard;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import study_com.studytogetherproject.Moduls.Item;
import study_com.study_exmp.studytogetherproject.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllTasksFragment extends Fragment {
    private Adapter adapter;
    private ImageView filters;
    private ArrayList<Item> arrayList = new ArrayList<>();
    private RecyclerView rv;
    private EditText edit_find;
    private TextView textMainAct;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        initView(root);
        recyclerBuilder();
        readUser();
        return root;
    }

    private void initView(View root){
        rv = root.findViewById(R.id.rv);
        edit_find = root.findViewById(R.id.edit_find);
        filters = root.findViewById(R.id.filters);
        textMainAct = root.findViewById(R.id.textMainAct);
        textMainAct.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
    }


    private void recyclerBuilder() {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searching() {
        edit_find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        Dialog dialog;
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edit_class = dialog.findViewById(R.id.find_class);
        Button textCancel = dialog.findViewById(R.id.yes);
        SeekBar seekBar = dialog.findViewById(R.id.seekbar);
        TextView textTotal = dialog.findViewById(R.id.textTotal);
        int color = ContextCompat.getColor(getContext(), R.color.main);
        int colorLine = ContextCompat.getColor(getContext(), R.color.main);
        seekBar.getProgressDrawable().setColorFilter(colorLine, PorterDuff.Mode.SRC_ATOP); // полоска
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP); // кругляшок

        edit_class.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterClass(s.toString());
            }
        });

        int min = 1;
        int max = 5;
        seekBar.setMax(max);
        seekBar.setMin(min);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 1) {
                    textTotal.setText(String.valueOf(100));
                } else if (progress == 2) {
                    textTotal.setText(String.valueOf(200));
                } else if (progress == 3) {
                    textTotal.setText(String.valueOf(300));
                } else if (progress == 4) {
                    textTotal.setText(String.valueOf(400));
                } else if (progress == 5) {
                    textTotal.setText(String.valueOf(500));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterPoints(s.toString());
            }
        });

        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void readUser() {

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("User").child(uid);

        ValueEventListener eventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subjectUser = snapshot.child("subject").getValue(String.class);

                ValueEventListener valueEventTask = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String subjectTask = null;
                        String describtion = null;
                        String nameTask = null;
                        String emailTask = null;
                        String imgPath = null;
                        String points = null;
                        String phone = null;
                        String dateToFinish = null;
                        String nameOfTask = null;
                        String classText = null;
                        String subjectToDetail = null;
                        String describeToDetail = null;
                        String id = null;
                        String idOfTask = null;
                        String imgUri1 = null;

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            subjectTask = ds.child("subject").getValue(String.class);
                            id = ds.child("id").getValue(String.class);
                            describtion = ds.child("describe").getValue(String.class);
                            nameTask = ds.child("name").getValue(String.class);
                            emailTask = ds.child("email").getValue(String.class);
                            imgPath = ds.child("img").getValue(String.class);
                            points = ds.child("points").getValue(String.class);
                            phone = ds.child("phone").getValue(String.class);
                            nameOfTask = ds.child("nameOfTask").getValue(String.class);
                            dateToFinish = ds.child("dateToFinish").getValue(String.class);
                            classText = ds.child("classText").getValue(String.class);
                            subjectToDetail = ds.child("subjectOfUser").getValue(String.class);
                            describeToDetail = ds.child("describtionOfUser").getValue(String.class);
                            idOfTask = ds.child("idOfTask").getValue(String.class);
                            imgUri1 = ds.child("imgUri1").getValue(String.class);


                            if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(emailTask)) {
                                if (subjectUser.equalsIgnoreCase(subjectTask)) {
                                    arrayList.add(new Item(nameTask, imgPath,
                                            subjectTask, points,
                                            describtion, emailTask, phone, nameOfTask,
                                            dateToFinish, classText, subjectToDetail, describeToDetail, id, idOfTask, imgUri1));

                                    adapter = new Adapter(getContext(), arrayList);
                                    rv.setAdapter(adapter);
                                    textMainAct.setVisibility(View.GONE);
                                    rv.setVisibility(View.VISIBLE);
                                    filters.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onClick(View v) {
                                            searching();
                                        }
                                    });
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


    //filters
    private void filter(String text) {
        ArrayList<Item> array = new ArrayList<>();
        for (Item item : arrayList) {
            if (item.getNameOfTask().toLowerCase().contains(text.toLowerCase())) {
                array.add(item);
            }
        }
        adapter.filterList(array);
    }

    private void filterPoints(String text) {
        ArrayList<Item> array = new ArrayList<>();
        for (Item item : arrayList) {
            if (item.getPoints().toLowerCase().contains(text.toLowerCase())) {
                array.add(item);
            }
        }
        adapter.filterList(array);
    }

    private void filterClass(String text) {
        ArrayList<Item> array = new ArrayList<>();
        for (Item item : arrayList) {
            if (item.getClassText().toLowerCase().contains(text.toLowerCase())) {
                array.add(item);
            }
        }
        adapter.filterList(array);
    }

}