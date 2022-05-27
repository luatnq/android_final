package com.example.contactfinal.activities;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerLauncher;
import com.esafirm.imagepicker.model.Image;
import com.example.contactfinal.MainActivity;
import com.example.contactfinal.dao.ContactDAO;
import com.example.contactfinal.dao.ContactService;
import com.example.contactfinal.databinding.AddContactBinding;
import com.example.contactfinal.model.Contact;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddContact extends AppCompatActivity {
    private AddContactBinding binding;

    private static final String TAG = "CONTACT_TAG";
    private static final int WRITE_CONTACT_PERMISSION_CODE = 100;

    private String[] contactPermissions;
    private Uri image_uri;

    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New contact");
        binding = AddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init permission array, need
        contactPermissions = new String[]{Manifest.permission.WRITE_CONTACTS};

        binding.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                saveContact();
                if (isWriteContactPermissionEnabled()) {
                    //permission already enabled, save contact
                    saveContact();
                    finish();
                } else {
                    //permission not enable, request
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveContact() {
        String firstName = binding.firstName.getText().toString().trim();
        String phoneNumber = binding.phoneNumber.getText().toString().trim();
        String address = binding.address.getText().toString().trim();
        String email = binding.email.getText().toString().trim();

        try {
            Log.d(TAG, "saveContact: Saved...");

            Contact contact = new Contact(firstName, email, address, phoneNumber, Objects.nonNull(image_uri) ? image_uri.toString() : null);
            ContactDAO contactDAO = new ContactDAO();
            contactDAO.add(contact);

            ContactService contactService = new ContactService();
            contactService.add(contact);

            Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "saveContact" + e.getMessage());
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isWriteContactPermissionEnabled() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestWriteContactPermission() {
        ActivityCompat.requestPermissions(this, contactPermissions, WRITE_CONTACT_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle permission results

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

    private void openGalleryIntent() {
        FishBun.with(this).setImageAdapter(new GlideAdapter()).startAlbumWithOnActivityResult(12);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 12) {

                Glide.with(this).load(data.getParcelableArrayListExtra(FishBun.INTENT_PATH).get(0)).into(binding.thumbnail);
                image_uri = (Uri) data.getParcelableArrayListExtra(FishBun.INTENT_PATH).get(0);
            }
        } else {
            Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}
