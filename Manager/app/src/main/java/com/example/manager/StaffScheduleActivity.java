package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.model.Staff;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StaffScheduleActivity extends AppCompatActivity {
    ImageView icNext;
    EditText edTimeStart, edTimeEnd;
    TextView tvTime, tvTime2, tvTime3, tvTime4, tvTime5, tvTime6, tvTime7;
    Button btnOK, btnRegister, btnC1, btnC2;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendar1, calendar2;
    DatabaseReference brRoot;
    String staffID;
    LinearLayout layoutTime;
    Spinner spinner;
    String[] time = {"1 tuần", "2 tuần"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_schedule);
        init();
        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        staffID = getIntent().getStringExtra("id");
        brRoot = FirebaseDatabase.getInstance().getReference("Staff");

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, time);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        edTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1();
            }
        });
//        edTimeEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePicker2();
//            }
//        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTime.setVisibility(View.VISIBLE);
                tvTime.setText(sdf.format(calendar1.getTime()));
            }
        });
        icNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar1.add(Calendar.DATE, 1);
                tvTime.setText(sdf.format(calendar1.getTime()));
            }
        });

        btnC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Staff staff = new Staff();
                staff.setShift1(true);
                brRoot.child(staffID).child("shedule").child(tvTime.getText().toString()).child("Ca sáng").setValue(staff, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(StaffScheduleActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btnC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Staff staff = new Staff();
                staff.setShift2(true);
                brRoot.child(staffID).child("shedule").child(tvTime.getText().toString()).child("Ca tối").setValue(staff, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(StaffScheduleActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void init() {
        spinner = findViewById(R.id.spinner);
        layoutTime = findViewById(R.id.layoutTime);
        edTimeStart = findViewById(R.id.edTimeStart);
        icNext = findViewById(R.id.icNext);
//        edTimeEnd = findViewById(R.id.edTimeEnd);
        tvTime = findViewById(R.id.tvTime);
        btnC1 = findViewById(R.id.btnC1);
        btnC2 = findViewById(R.id.btnC2);
//        tvTime2 = findViewById(R.id.tvTime2);
//        tvTime3 = findViewById(R.id.tvTime3);
//        tvTime4 = findViewById(R.id.tvTime4);
//        tvTime5 = findViewById(R.id.tvTime5);
//        tvTime6 = findViewById(R.id.tvTime6);
//        tvTime7 = findViewById(R.id.tvTime7);
        btnOK = findViewById(R.id.btnOK);
        btnRegister = findViewById(R.id.btnRegister);
    }

    public void datePicker1() {
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        final int day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar1.set(year, month, dayOfMonth);
                edTimeStart.setText(sdf.format(calendar1.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void datePicker2() {
        int year = calendar2.get(Calendar.YEAR);
        int month = calendar2.get(Calendar.MONTH);
        final int day = calendar2.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar2.set(year, month, dayOfMonth);
                edTimeEnd.setText(sdf.format(calendar2.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}