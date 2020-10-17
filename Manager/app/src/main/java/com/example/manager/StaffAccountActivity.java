package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manager.model.Staff;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Stack;

public class StaffAccountActivity extends AppCompatActivity {
    ImageView imgAvatar;
    EditText edName, edPhone, edBirthday;
    TextView tvUsername;
    Button btnUpdateInfo, btnChangeAvatar;
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
        setTitle("Tài khoản của tôi");
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
        keyID = getIntent().getStringExtra("staffID");

        tvUsername.setText(getIntent().getStringExtra("staffID"));
        LoadData();
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(StaffAccountActivity.this);
                View view = LayoutInflater.from(StaffAccountActivity.this).inflate(R.layout.dialog_choose_avatar, null);
                builder.setTitle("Chọn ảnh");
                ImageView icCamera = view.findViewById(R.id.icCamera);
                ImageView icImage = view.findViewById(R.id.icImage);
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
                builder.setView(view);
                builder.show();
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
                                final Staff staff = new Staff();
                                staff.setStaffName(edName.getText().toString());
                                staff.setPhone(edPhone.getText().toString());
                                staff.setAvatarLink(uri.toString());
                                staff.setBirthday(sdf.format(calendar1.getTime()));
//                                final String linkAnh = uri.toString();
                                mData.child(keyID).child("name").setValue(edName.getText().toString());
                                mData.child(keyID).child("phone").setValue(edPhone.getText().toString());
                                mData.child(keyID).child("avatarLink").setValue(uri.toString());
                                mData.child(keyID).child("birthday").setValue(sdf.format(calendar1.getTime()));
                            }
                        });
                    }

                }
            }
        });
    }


    private void init() {
        imgAvatar = findViewById(R.id.imgAvatar);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edBirthday = findViewById(R.id.edBirthday);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        tvUsername = findViewById(R.id.tvUsername);
        btnChangeAvatar = findViewById(R.id.btnChangeAvt);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_register_schedule) {
            Intent intent = new Intent(StaffAccountActivity.this, StaffScheduleActivity.class);
            intent.putExtra("id", keyID);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadData() {
        DatabaseReference brRoot = FirebaseDatabase.getInstance().getReference("Staff").child(keyID);
        brRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String name = areaSnapshot.child("name").getValue(String.class);
                    String birthday = areaSnapshot.child("birthday").getValue(String.class);
                    final String[] nameList = {name};
                    edName.setText(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        Glide.with(StaffAccountActivity.this).load(mData.child(keyID).child("avatarLink").toString())
//                .skipMemoryCache(false)
//                .into(imgAvatar);
//        edName.setText(mData.child(keyID).child("name").toString());
//        edBirthday.setText(mData.child(keyID).child("birthday") + "");
        edPhone.setText(mData.child(keyID).child("name").getKey());
//        Toast.makeText(getApplicationContext(), mData.child(keyID).child("avatarLink").toString() + "acd", Toast.LENGTH_SHORT).show();
    }
}