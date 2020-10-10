package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;

public class SignupWithPhoneActivity extends AppCompatActivity {
    TextInputEditText edName, edPhone, edUsername, edPassword, edRePassword;
    Button btnContinue;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_phone);
        setTitle("Sign up an account");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        init();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });
    }

    private void init() {
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        edRePassword = findViewById(R.id.edRePassword);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void Signup() {
        if (!edName.getText().toString().isEmpty() && !edPhone.getText().toString().isEmpty() || !edUsername.getText().toString().isEmpty() || !edPassword.getText().toString().isEmpty()) {
            Intent intent = new Intent(SignupWithPhoneActivity.this, VerifyPhoneActivity.class);
            String phone_number = "+84" + edPhone.getText().toString().trim();
            intent.putExtra("phone_number", phone_number);
            intent.putExtra("name", edName.getText().toString());
            intent.putExtra("username", edUsername.getText().toString());
            intent.putExtra("password", edPassword.getText().toString());
            startActivity(intent);
        } else {

        }
    }
}