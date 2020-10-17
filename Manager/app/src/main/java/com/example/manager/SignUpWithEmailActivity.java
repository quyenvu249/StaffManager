package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manager.model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpWithEmailActivity extends AppCompatActivity {
    TextInputEditText edEmail, edPassword, edRepassword;
    Button btnSignup;
    private FirebaseAuth mAuth;
    String staffID;
    DatabaseReference mData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        mData = FirebaseDatabase.getInstance().getReference().child("Staff");

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        staffID = sharedPreferences.getString("key", "");
    }

    public void dangKy() {
        final String email = edEmail.getText().toString();
        final String pw = edPassword.getText().toString();
        final String rpw = edRepassword.getText().toString();
        if (pw.equals(rpw)) {
            mAuth.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpWithEmailActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                String[] strings = edEmail.getText().toString().split("@");
                                staffID = strings[0];
//                                editor = sharedPreferences.edit();
//                                editor.putString("key", staffID);
//                                editor.commit();
                                Staff staff = new Staff();
                                staff.setStaffID(staffID);
                                staff.setEmail(email);
                                staff.setPassword(pw);
                                mData.child(staffID).setValue(staff, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Log.d("Login", "Successful");
                                    }
                                });
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(SignUpWithEmailActivity.this, LoginActivity.class);
                                        intent.putExtra("staffID", staffID);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 500);
                            } else {
                                Toast.makeText(SignUpWithEmailActivity.this, "Đăng kí không thành công" + task.getException(), Toast.LENGTH_SHORT).show();
                                Log.d("Error", task.getException().toString());
                            }
                        }
                    });
        } else {
            Toast.makeText(SignUpWithEmailActivity.this, "Kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }


}