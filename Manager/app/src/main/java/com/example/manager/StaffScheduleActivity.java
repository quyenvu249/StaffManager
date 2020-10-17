package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.model.Schedule;
import com.example.manager.model.Staff;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StaffScheduleActivity extends AppCompatActivity {
    ImageView icNext;
    EditText edTimeStart, edTimeEnd;
    TextView tvTime;
    CheckBox cbShift1, cbShift2;
    Button btnOK, btnRegister, btnC1, btnC2;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendar1, calendar2;
    DatabaseReference brRoot;
    String staffID;
    LinearLayout layoutTime;
    Spinner spinner;
    String[] time = {"1 tuần", "2 tuần"};
    ArrayList<Schedule> schedules;
    ScheduleAdapter adapter;
    ListView lvShift;
    Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_schedule);
        init();
        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        staffID = getIntent().getStringExtra("staffID");
        brRoot = FirebaseDatabase.getInstance().getReference("Staff");
        schedule = new Schedule();


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, time);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        edTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1();
            }
        });
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
        schedules = new ArrayList<>();
        adapter = new ScheduleAdapter(StaffScheduleActivity.this, schedules, R.layout.item_schedule);
        lvShift.setAdapter(adapter);
        LoadData();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbShift1.isChecked() && !cbShift2.isChecked()) {
                    schedule = new Schedule(tvTime.getText().toString(), true, false);
                } else if (cbShift2.isChecked() && !cbShift1.isChecked()) {
                    schedule = new Schedule(tvTime.getText().toString(), false, true);
                } else if (cbShift1.isChecked() && cbShift2.isChecked()) {
                    schedule = new Schedule(tvTime.getText().toString(), true, true);
                } else {
                    schedule = new Schedule(tvTime.getText().toString(), false, false);
                }
                brRoot.child(staffID).child("shedule").child(tvTime.getText().toString()).setValue(schedule, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(StaffScheduleActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

//        btnC1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Schedule schedule = new Schedule();
//                schedule.date = tvTime.getText().toString();
//                schedule.setShift1("Ca sáng");
//                brRoot.child(staffID).child("shedule").setValue(schedule, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                        Toast.makeText(StaffScheduleActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
//                        schedules.add(schedule);
//                        adapter = new ScheduleAdapter(StaffScheduleActivity.this, schedules, R.layout.item_schedule);
//                        lvShift.setAdapter(adapter);
//                        adapter = new ScheduleAdapter(StaffScheduleActivity.this, schedules, R.layout.item_schedule);
//                        lvShift.setAdapter(adapter);
//                        LoadData();
//                    }
//                });
//            }
//        });
//        btnC2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Schedule schedule = new Schedule();
//                schedule.date = tvTime.getText().toString();
//                schedule.setShift1("Ca sáng");
//                brRoot.child(staffID).child("shedule").setValue(schedule, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                        Toast.makeText(StaffScheduleActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
//                        schedule.date = tvTime.getText().toString();
//                        schedule.setShift2("Ca tối");
//                        schedules.add(schedule);
//                    }
//                });
//            }
//        });

    }


    private void init() {
        spinner = findViewById(R.id.spinner);
        layoutTime = findViewById(R.id.layoutTime);
        edTimeStart = findViewById(R.id.edTimeStart);
        icNext = findViewById(R.id.icNext);
        lvShift = findViewById(R.id.lvShift);
        tvTime = findViewById(R.id.tvTime);
        cbShift1 = findViewById(R.id.cbShift1);
        cbShift2 = findViewById(R.id.cbShift2);
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

    private void LoadData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            brRoot.child(staffID).child("shedule").child(tvTime.getText().toString()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    schedules.add(new Schedule(schedule.date,schedule.dayShift, schedule.nightShift));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 10);
        }

    }
}