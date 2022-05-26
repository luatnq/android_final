package com.example.contactfinal.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactfinal.R;
import com.example.contactfinal.activities.UpdateContact;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.bumptech.glide.Glide;
import com.example.contactfinal.model.Contact;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends FirebaseRecyclerAdapter<Contact, ContactAdapter.ContactViewHolder> {


    public ContactAdapter(@NonNull FirebaseRecyclerOptions<Contact> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ContactViewHolder holder, int position, @NonNull Contact contact) {

        holder.fullName.setText(contact.getFirstName().concat(" ".concat(contact.getLastName())));
        holder.email.setText("" + contact.getEmail());
        holder.phoneNumber.setText(contact.getPhoneNumber());

        if (contact.getImageUrl() != null) {
            Glide.with(holder.avatar.getContext()).load(contact.getImageUrl().trim()).error(R.drawable.ic_person).into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_person);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UpdateContact.class);
                contact.setKey(getRef(position).getKey());
                putData(intent, contact);
                intent.putExtra("key", getRef(position).getKey());
                // start the Intent
                view.getContext().startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.avatar.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Bạn có muốn xóa...?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Contact")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    private void putData(Intent intent, Contact contact){
        intent.putExtra("firstName", contact.getFirstName());
        intent.putExtra("lastName", contact.getLastName());
        intent.putExtra("phoneNumber", contact.getPhoneNumber());
        intent.putExtra("avatarUrl", contact.getImageUrl());
        intent.putExtra("email", contact.getEmail());
        intent.putExtra("address", contact.getAddress());

    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView firstName, lastName, phoneNumber, email, address, fullName;
        ImageView btnEdit, btnDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            fullName = (TextView) itemView.findViewById(R.id.contact_name);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = (TextView) itemView.findViewById(R.id.lastName);
            phoneNumber = (TextView) itemView.findViewById(R.id.contact_number);
            email = itemView.findViewById(R.id.contact_email);
            btnEdit =  itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);


        }
    }
}
