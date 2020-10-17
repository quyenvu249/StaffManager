package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class StaffActivity extends AppCompatActivity {
    String staffID;
    LinearLayout registerSchedule, settings, staffAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        staffID = getIntent().getStringExtra("staffID");

        setTitle("Xin chào " + staffID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerSchedule = findViewById(R.id.registerSchedule);
        staffAccount = findViewById(R.id.staffAccount);
        registerSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, StaffScheduleActivity.class);
                intent.putExtra("staffID", getIntent().getStringExtra("staffID"));
                startActivity(intent);
            }
        });
        staffAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, StaffAccountActivity.class);
                intent.putExtra("staffID", getIntent().getStringExtra("staffID"));
                startActivity(intent);
            }
        });
    }
}