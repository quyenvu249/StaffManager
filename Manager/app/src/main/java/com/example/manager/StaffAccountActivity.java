package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.model.Staff;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Stack;

public class StaffAccountActivity extends AppCompatActivity {
    ImageView icCamera, icImage, imgAvatar;
    EditText edName, edPhone, edBirthday;
    Button btnUpdateInfo, btnRegister;
    int REQUEST_CODE_IMAGE = 100;
    int REQUEST_CODE_IMAGE_STORAGE = 200;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference mData;
    String keyID;
    StorageReference storageRef;
    Bitmap bitmap;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendar1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_account);
        init();
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

        calendar1 = Calendar.getInstance();

        mData = FirebaseDatabase.getInstance().getReference("Staff");

        storageRef = storage.getReferenceFromUrl("gs://staff-manager-8e972.appspot.com");

        icCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicture();
            }
        });
        icImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        edBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1();
            }
        });
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffAccountActivity.this, StaffScheduleActivity.class);
                intent.putExtra("id", keyID);
                startActivity(intent);
            }
        });
    }

    private void updateInfo() {
        Calendar calendar = Calendar.getInstance();
        final StorageReference mountainsRef = storageRef.child("avatar" + calendar.getTimeInMillis() + ".png");
        imgAvatar.setDrawingCacheEnabled(true);
        imgAvatar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(StaffAccountActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        Toast.makeText(StaffAccountActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                keyID = mData.push().getKey();
                                final Staff staff = new Staff();
                                staff.setStaffID(keyID);
                                staff.setStaffName(edName.getText().toString());
                                staff.setBirthday(sdf.format(calendar1.getTime()));
                                final String linkAnh = uri.toString();
                                mData.child(keyID).setValue(staff, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Log.d("key", keyID);
                                        if (databaseError == null) {
                                            Toast.makeText(StaffAccountActivity.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
                                            edName.setText(staff.getStaffName());
                                            edBirthday.setText(staff.getBirthday());
                                            edPhone.setText(staff.getPhone());
                                            imgAvatar.setImageURI(uri);
                                        } else {
                                            Toast.makeText(StaffAccountActivity.this, "Lỗi!!!" + databaseError, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        });

                    }
                }


            }
        });
    }

    private void init() {
        icCamera = findViewById(R.id.icCamera);
        icImage = findViewById(R.id.icImage);
        imgAvatar = findViewById(R.id.imgAvatar);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edBirthday = findViewById(R.id.edBirthday);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void choosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE_IMAGE_STORAGE);
    }

    //xử lý chụp hình
    private void capturePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            //xử lí h.ả lúc chụp
            bitmap = (Bitmap) data.getExtras().get("data");
            imgAvatar.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_CODE_IMAGE_STORAGE && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void datePicker1() {
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        final int day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar1.set(year, month, dayOfMonth);
                edBirthday.setText(sdf.format(calendar1.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}