package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class StaffActivity extends AppCompatActivity {
    String name;
    LinearLayout registerSchedule, settings, staffAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        name = getIntent().getStringExtra("username");

        setTitle("Xin chào! ");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //registerSchedule = findViewById(R.id.registerSchedule);
        staffAccount = findViewById(R.id.staffAccount);
//        registerSchedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(StaffActivity.this, StaffScheduleActivity.class));
//            }
//        });
        staffAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffActivity.this, StaffAccountActivity.class));
            }
        });
    }
}