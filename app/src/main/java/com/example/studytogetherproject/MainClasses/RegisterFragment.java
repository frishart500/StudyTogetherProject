package com.example.studytogetherproject.MainClasses;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studytogetherproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;


public class RegisterFragment extends Fragment {
    private CardView cs1, cs2, cs3, cs4, cs5, cs6, cs7;
    private TextView reg;
    private ImageView dataImg;
    private Button reg1;
    private EditText email, password, name, phone, data, describtion;
    private TextView subject, back;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    // TODO: Rename parameter arguments, choose names that match
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.list_of_subject);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                ListView list = dialog.findViewById(R.id.list);
                ImageView close = dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                String[] subjectsArray = getResources().getStringArray(R.array.subjects);

                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.item_for_list, R.id.textSubject, subjectsArray);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        subject.setText(subjectsArray[position]);
                    }
                });

                dialog.show();
            }
        });
        setOnClickReg();
        getDataPicker();

    return view;
    }

    private void getDataPicker() {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                String m = String.valueOf(month);
                String d = String.valueOf(day);
                System.out.println("MONTH " + month);
                if (String.valueOf(day).length() == 1) {
                    d = ("0" + day);
                }
                if (String.valueOf(month).length() == 1) {
                    m = ("0" + month);
                }
                date = d + "." + m + "." + year;
                data.setText(date);
            }
        };

        dataImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void init(View view) {
        reg = view.findViewById(R.id.registrBtn);
        reg1 = view.findViewById(R.id.registrBtn1);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        name = view.findViewById(R.id.name);
        describtion = view.findViewById(R.id.describe);
        phone = view.findViewById(R.id.phone);
        data = view.findViewById(R.id.editData);
        subject = view.findViewById(R.id.subject);
        dataImg = view.findViewById(R.id.dataImg);
        back = view.findViewById(R.id.back);
        back.setPaintFlags(back.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        reg.setPaintFlags(reg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cs1 = view.findViewById(R.id.cardView1);
        cs2 = view.findViewById(R.id.cardView2);
        cs3 = view.findViewById(R.id.cardView3);
        cs4 = view.findViewById(R.id.cardView4);

        cs5 = view.findViewById(R.id.cardView5);
        cs6 = view.findViewById(R.id.cardView6);
        cs7 = view.findViewById(R.id.cardView7);
    }

    private void setOnClickReg() {
        reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Регистрация...", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(0XFFffffff);
                snackbar.setTextColor(0XFF601C80);
                snackbar.show();
                registration(email.getText().toString().trim(), password.getText().toString().trim(), v);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg.setVisibility(View.GONE);
                cs1.setVisibility(View.GONE);
                cs2.setVisibility(View.GONE);
                cs3.setVisibility(View.GONE);
                cs4.setVisibility(View.GONE);

                back.setVisibility(View.VISIBLE);
                reg1.setVisibility(View.VISIBLE);
                cs5.setVisibility(View.VISIBLE);
                cs6.setVisibility(View.VISIBLE);
                cs7.setVisibility(View.VISIBLE);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg.setVisibility(View.VISIBLE);
                cs1.setVisibility(View.VISIBLE);
                cs2.setVisibility(View.VISIBLE);
                cs3.setVisibility(View.VISIBLE);
                cs4.setVisibility(View.VISIBLE);

                back.setVisibility(View.GONE);
                reg1.setVisibility(View.GONE);
                cs5.setVisibility(View.GONE);
                cs6.setVisibility(View.GONE);
                cs7.setVisibility(View.GONE);
            }
        });
    }

    private void registration(String emailS, String passwordS, View v) {
        if (!emailS.isEmpty() && !data.getText().toString().isEmpty() && !passwordS.isEmpty() && passwordS.length() >= 6 && !name.getText().toString().isEmpty() && !describtion.getText().toString().isEmpty() && !subject.getText().toString().equals("Твоя специализация") && !phone.getText().toString().isEmpty()) {
            Intent intent = new Intent(getContext(), ChooseGenderActivity.class);
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("password", passwordS);
            putIntent(intent);
            startActivity(intent);
        }else {
            Snackbar snackbar = Snackbar.make(v, "Регистрация провалена! Вы долны ввести всю информацию.", Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(0XFF601C80);
            snackbar.setTextColor(0XFFffffff);
            snackbar.show();
        }
    }

    private void putIntent(Intent intent) {
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("data", data.getText().toString());
        intent.putExtra("describtion", describtion.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.putExtra("subject", subject.getText().toString());
        intent.putExtra("imgUri", "");
        intent.putExtra("points", "500");
        intent.putExtra("imgUri", "0");
        intent.putExtra("average", "5.0");
        intent.putExtra("howMuchTaskDone", "0");
        intent.putExtra("howMuchNotifications", "0");
    }
}

