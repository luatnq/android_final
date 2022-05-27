package com.example.contactfinal.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.contactfinal.model.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactService {

    FirebaseFirestore firebaseFirestore;

    public void add(Contact contact) {
        Map<String, String> items = new HashMap<>();
        items.put("firstName", contact.getName());
        items.put("address", contact.getAddress());
        items.put("email", contact.getEmail());
        items.put("imageUrl", contact.getImageUrl());
        items.put("key", contact.getKey());
        items.put("phoneNumber", contact.getPhoneNumber());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Contact").add(items).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST: ", "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEST: ", "Error adding document", e);
                    }
                });
        ;
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Contact").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Contact> contacts1 = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TEST === get", document.getId() + " => " + document.getData());
                                contacts1.add(new Contact(document.getId(),
                                        document.get("firstName", String.class),
                                        document.get("email", String.class),
                                        document.get("address", String.class),
                                        document.get("phoneNumber", String.class),
                                        document.get("imageUrl", String.class)));
                                Log.d("TEST: data", document.get("firstName", String.class));
                            }
                            contacts.addAll(contacts1);
                        } else {
                            Log.w("TEST === get", "Error getting documents.", task.getException());
                        }
                    }
                });
        return contacts;
    }

    public void get(List<Contact> contacts, List<Contact> contacts2){
        contacts = new ArrayList<>(contacts2);
    }
}
