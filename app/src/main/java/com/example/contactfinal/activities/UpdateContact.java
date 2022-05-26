package com.example.contactfinal.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.contactfinal.MainActivity;
import com.example.contactfinal.R;
import com.example.contactfinal.dao.ContactDAO;
import com.example.contactfinal.databinding.ContactDetailBinding;
import com.example.contactfinal.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.HashMap;
import java.util.Objects;

public class UpdateContact extends AppCompatActivity {

    //UI Views
    private ImageView thumbnailIv;
    private TextView firstName, lastName, phoneNumber, address, email;
    private FloatingActionButton btnEdit, btnBack;
    private ContactDetailBinding binding;

    private Uri image_uri;
    private String[] contactPermissions;

    private static int CONTACT_PERMISSION_CODE = 1;
    private static int CONTACT_PICK_CODE = 2;
    private static int CONTACT_PICK_IMAGE = 12;
    private static final int WRITE_CONTACT_PERMISSION_CODE = 100;
    private static final String TAG = "CONTACT_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Update contact");
        binding = ContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        init(intent);

        contactPermissions = new String[]{Manifest.permission.WRITE_CONTACTS};
        binding.thumbnailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //check read contact permission
                if (isWriteContactPermissionEnabled()) {
                    //permission granted, pick contact
                    saveContact();
                    finish();
                } else {
                    //permission not granted, request
                    requestWriteContactPermission();
                }
            }
        });


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private boolean isWriteContactPermissionEnabled() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestWriteContactPermission() {
        ActivityCompat.requestPermissions(this, contactPermissions, WRITE_CONTACT_PERMISSION_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveContact() {
        String firstName = binding.firstNameEdit.getText().toString().trim();
        String lastName = binding.lastNameEdit.getText().toString().trim();
        String phoneNumber = binding.phoneNumberEdit.getText().toString().trim();
        String address = binding.addressEdit.getText().toString().trim();
        String email = binding.emailEdit.getText().toString().trim();

        try {
            Log.d(TAG, "saveContact: Saved...");

            ContactDAO contactDAO = new ContactDAO();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("firstName", firstName);
            hashMap.put("lastName", lastName);
            hashMap.put("phoneNumber", phoneNumber);
            hashMap.put("address", address);
            hashMap.put("email", email);
            hashMap.put("key", getIntent().getStringExtra("key"));
            hashMap.put("imageUrl", image_uri.toString());


            contactDAO.update(getIntent().getStringExtra("key"), hashMap).addOnSuccessListener(suc -> {
                Toast.makeText(this, "Record is updated", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener( er -> {
                Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "saveContact" + e.getMessage());
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle permission request
        if (grantResults.length > 0) {
            if (requestCode == WRITE_CONTACT_PERMISSION_CODE) {
                boolean haveWriteContactPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (haveWriteContactPermission) {
                    saveContact();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle intent results
        if (resultCode == RESULT_OK) {
            //call when user click a contact from list
            if (requestCode == CONTACT_PICK_IMAGE) {

                Glide.with(this).load(data.getParcelableArrayListExtra(
                        FishBun.INTENT_PATH).get(0)).into(binding.thumbnailEdit);
                image_uri = (Uri) data.getParcelableArrayListExtra(FishBun.INTENT_PATH).get(0);
            }
        } else {
            //call when user click back button | don't pick contact
        }
    }

    private void openGalleryIntent() {
        FishBun.with(this).setImageAdapter(new GlideAdapter()).startAlbumWithOnActivityResult(CONTACT_PICK_IMAGE);
    }


    private void init(Intent intent) {
        thumbnailIv = findViewById(R.id.thumbnailEdit);
        firstName = findViewById(R.id.firstNameEdit);
        lastName = findViewById(R.id.lastNameEdit);
        phoneNumber = findViewById(R.id.phoneNumberEdit);
        address = findViewById(R.id.addressEdit);
        email = findViewById(R.id.emailEdit);
        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);

        firstName.setText(intent.getStringExtra("firstName"));
        lastName.setText(intent.getStringExtra("lastName"));
        phoneNumber.setText(intent.getStringExtra("phoneNumber"));
        address.setText(intent.getStringExtra("address"));
        email.setText(intent.getStringExtra("email"));
        Glide.with(this).load(intent.getStringExtra("avatarUrl")).into(thumbnailIv);
    }
}
