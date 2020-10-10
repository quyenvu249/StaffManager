package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private String verificationId;
    TextInputEditText edCode;
    Button btnSignup;
    FirebaseAuth firebaseAuth;
    String name, username;
    String role, password;
    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        setTitle("Verify Phone");
        firebaseAuth = FirebaseAuth.getInstance();
        edCode = findViewById(R.id.edCode);
        btnSignup = findViewById(R.id.btnSignup);

        phone_number = getIntent().getStringExtra("phone_number");
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");
        password = getIntent().getStringExtra("password");

        sendVerificationCode(phone_number);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    edCode.setError("Enter code...");
                    edCode.requestFocus();
                }
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signUpWithCredential(credential);
    }

    private void signUpWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (role.equals("Admin")) {
                        Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("name", name);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("name", name);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("error", e.getMessage());
            edCode.setText(e.getMessage());
        }


    };
}