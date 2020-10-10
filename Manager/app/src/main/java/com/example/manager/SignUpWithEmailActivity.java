package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpWithEmailActivity extends AppCompatActivity {
    TextInputEditText edEmail, edPassword, edRepassword;
    Button btnSignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Đăng kí");
        setContentView(R.layout.activity_sign_up_with_email);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edRepassword = findViewById(R.id.edRePassword);
        btnSignup = findViewById(R.id.btnSignup);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });

    }

    public void dangKy() {
        String email = edEmail.getText().toString();
        final String pw = edPassword.getText().toString();
        final String rpw = edRepassword.getText().toString();
//        if (email.isEmpty() || pw.isEmpty() || rpw.isEmpty() || edName.getText().toString().isEmpty()) {
        if (pw.equals(rpw)) {
            mAuth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpWithEmailActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(SignUpWithEmailActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 500);
                            } else {
                                Toast.makeText(SignUpWithEmailActivity.this, "Đăng kí không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(SignUpWithEmailActivity.this, "Kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }


}