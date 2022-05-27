package com.example.contactfinal.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactfinal.R;
import com.example.contactfinal.activities.UpdateContact;
import com.example.contactfinal.model.Contact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapterFake extends RecyclerView.Adapter<ContactAdapterFake.ContactViewHolder> {

    private Context context;
    private List<Contact> contacts;

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ContactAdapterFake(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    public ContactAdapterFake(Context context) {
        this.contacts = new ArrayList<>();
        this.context = context;
    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Contact contact) {

//        holder.fullName.setText(contact.getFirstName().concat(" ".concat(contact.getLastName())));
//        holder.email.setText("" + contact.getEmail());
//        holder.phoneNumber.setText(contact.getPhoneNumber());
//
//        if (contact.getImageUrl() != null) {
//            Glide.with(holder.avatar.getContext()).load(contact.getImageUrl().trim()).error(R.drawable.ic_person).into(holder.avatar);
//        } else {
//            holder.avatar.setImageResource(R.drawable.ic_person);
//        }
//
//        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), UpdateContact.class);
////                contact.setKey(getRef(position).getKey());
////                putData(intent, contact);
////                intent.putExtra("key", getRef(position).getKey());
//                // start the Intent
//                view.getContext().startActivity(intent);
//            }
//        });
//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(holder.avatar.getContext());
//                builder.setTitle("Delete Panel");
//                builder.setMessage("Bạn có muốn xóa...?");
//                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        FirebaseDatabase.getInstance().getReference().child("Contact")
////                                .child(getRef(position).getKey()).removeValue();
//                    }
//                });
//
//                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.show();
//            }
//        });
//    }

    private void putData(Intent intent, Contact contact) {
        intent.putExtra("firstName", contact.getName());
        intent.putExtra("phoneNumber", contact.getPhoneNumber());
        intent.putExtra("avatarUrl", contact.getImageUrl());
        intent.putExtra("email", contact.getEmail());
        intent.putExtra("address", contact.getAddress());

    }

    @NonNull
    @Override
    public ContactAdapterFake.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactAdapterFake.ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        Contact contact = contacts.get(position);

        holder.fullName.setText(contact.getName().trim());
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
//                contact.setKey(getRef(position).getKey());
//                putData(intent, contact);
//                intent.putExtra("key", getRef(position).getKey());
                // start the Intent
                view.getContext().startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.avatar.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Bạn có muốn xóa...?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        FirebaseDatabase.getInstance().getReference().child("Contact")
//                                .child(getRef(position).getKey()).removeValue();
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

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView firstName, lastName, phoneNumber, email, fullName;
        ImageView btnEdit, btnDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            fullName = (TextView) itemView.findViewById(R.id.contact_name);
            firstName = itemView.findViewById(R.id.firstName);
            phoneNumber = (TextView) itemView.findViewById(R.id.contact_number);
            email = itemView.findViewById(R.id.contact_email);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
