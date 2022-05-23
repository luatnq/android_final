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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactfinal.dao.ContactDAO;
import com.example.contactfinal.databinding.AddContactBinding;
import com.example.contactfinal.model.Contact;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class AddContact extends AppCompatActivity {
    private AddContactBinding binding;

    private static final String TAG = "CONTACT_TAG";
    private static final int WRITE_CONTACT_PERMISSION_CODE = 100;
    private static final int IMAGE_PICK_GALLERY_CODE = 100;

    private String[] contactPermissions;

    private Uri image_uri;


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
                } else {
                    //permission not enable, request
                    requestWriteContactPermission();
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveContact() {
        String firstName = binding.firstName.getText().toString().trim();
        String lastName = binding.lastName.getText().toString().trim();
        String phoneNumber = binding.phoneNumber.getText().toString().trim();
        String address = binding.address.getText().toString().trim();
        String email = binding.email.getText().toString().trim();

        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();
        int rawContactId = cpo.size();

        cpo.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //add First name, Last name
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName).build());

        //add phone number
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        //add email
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());

        //add address
        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA, address)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK).build());

        byte[] imageBytes = imageUriBytes();
        if (imageBytes != null) {
            //contact with image
            //add image
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, imageBytes).build());
        } else {
            //contact without image
        }
        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, cpo);
            Log.d(TAG, "saveContact: Saved...");

            Contact contact = new Contact(firstName, lastName, email, address, phoneNumber, Objects.nonNull(image_uri) ? image_uri.toString() : null);
            ContactDAO contactDAO = new ContactDAO();
            contactDAO.add(contact);

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

    private byte[] imageUriBytes() {
        Bitmap bitmap;
        ByteArrayOutputStream baos = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);

            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        } catch (Exception e) {
            Log.d(TAG, "imageUriToBytes: " + e.getMessage());
            return null;
        }
        return baos.toByteArray();
    }

    private void openGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            image_uri = data.getData();

            binding.thumbnail.setImageURI(image_uri);
        } else {
            Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}
