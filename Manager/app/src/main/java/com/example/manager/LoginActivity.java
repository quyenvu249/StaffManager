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
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.manager.model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edUsername, edPassword;
    Button btnLogin, btnSignupWithPhone, btnSignupWithEmail;
    CheckBox cbSave;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    DatabaseReference mData;
    String userID;
    String[] strings;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        btnSignupWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpWithEmailActivity.class));
            }
        });

        sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE);
        edUsername.setText(sharedPreferences.getString("username", ""));
        edPassword.setText(sharedPreferences.getString("password", ""));
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Staff");
    }

    private void init() {
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignupWithEmail = findViewById(R.id.btnSignupWithEmail);
        //btnSignupWithPhone = findViewById(R.id.btnSignupWithPhone);
        cbSave = findViewById(R.id.cbSave);
    }

    public void logIn() {
        String tk = edUsername.getText().toString();
        String mk = edPassword.getText().toString();
        if (tk.equals("admin") && mk.equals("admin")) {
            if (cbSave.isChecked()) {
                editor = sharedPreferences.edit();
                editor.putString("username", edUsername.getText().toString().trim());
                editor.putString("password", edPassword.getText().toString().trim());
                Toast.makeText(LoginActivity.this, "Đã lưu mật khẩu", Toast.LENGTH_SHORT).show();
                editor.commit();
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        } else {
            mAuth.signInWithEmailAndPassword(tk, mk)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = mAuth.getCurrentUser();
                                strings = edUsername.getText().toString().split("@");
                                userID = strings[0];
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công" + userID,
                                        Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, StaffActivity.class);
                                        intent.putExtra("staffID", userID);
                                        intent.putExtra("username", edUsername.getText().toString());
                                        intent.putExtra("password", edPassword.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 500);

                                if (cbSave.isChecked()) {
                                    editor = sharedPreferences.edit();
                                    editor.putString("username", edUsername.getText().toString().trim());
                                    editor.putString("password", edPassword.getText().toString().trim());
                                    Toast.makeText(LoginActivity.this, "Đã lưu mật khẩu", Toast.LENGTH_SHORT).show();
                                    editor.commit();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, kiểm tra lại email và password" + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG", task.getException().toString());
                            }

                        }
                    });
        }

    }

}