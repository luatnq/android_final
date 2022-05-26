package com.example.contactfinal.dao;

import com.example.contactfinal.model.Contact;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ContactDAO {
    private DatabaseReference databaseReference;

    private static String CHILD_NAME = "Contact";

    public ContactDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Contact.class.getSimpleName());
    }
    public Task<Void> add(Contact contact){
        String key = databaseReference.child(CHILD_NAME).push().getKey();
        contact.setKey(key);
        return databaseReference.push().setValue(contact);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){
        System.out.println("Key========test" + key);
        return databaseReference.child(key).updateChildren(hashMap);
    }

//    public Task<Void> delete(String key, HashMap<String, Object> hashMap){
//        return databaseReference.;
//    }

}
