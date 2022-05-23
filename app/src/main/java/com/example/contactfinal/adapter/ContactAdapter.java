package com.example.contactfinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactfinal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.bumptech.glide.Glide;
import com.example.contactfinal.model.Contact;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends FirebaseRecyclerAdapter<Contact, ContactAdapter.ContactViewHolder> {


    public ContactAdapter(@NonNull FirebaseRecyclerOptions<Contact> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ContactViewHolder holder, int position, @NonNull Contact contact) {
        holder.firstName.setText("" + contact.getFirstName());
//        holder.lastName.setText("" + contact.getLastName());
        holder.email.setText("" + contact.getEmail());
        Glide.with(holder.avatar.getContext()).load(contact.getImageBytes()).into(holder.avatar);

//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
//                        .setExpanded(true,1100)
//                        .create();
//
//                View myview=dialogPlus.getHolderView();
//                final EditText purl=myview.findViewById(R.id.uimgurl);
//                final EditText name=myview.findViewById(R.id.uname);
//                final EditText course=myview.findViewById(R.id.ucourse);
//                final EditText email=myview.findViewById(R.id.uemail);
//                Button submit=myview.findViewById(R.id.usubmit);
//
//                purl.setText(model.getPurl());
//                name.setText(model.getName());
//                course.setText(model.getCourse());
//                email.setText(model.getEmail());
//
//                dialogPlus.show();
//
//                submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Map<String,Object> map=new HashMap<>();
//                        map.put("purl",purl.getText().toString());
//                        map.put("name",name.getText().toString());
//                        map.put("email",email.getText().toString());
//                        map.put("course",course.getText().toString());
//
//                        FirebaseDatabase.getInstance().getReference().child("students")
//                                .child(getRef(position).getKey()).updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        dialogPlus.dismiss();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        dialogPlus.dismiss();
//                                    }
//                                });
//                    }
//                });
//
//
//            }
//        });


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
//                builder.setTitle("Delete Panel");
//                builder.setMessage("Delete...?");
//
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        FirebaseDatabase.getInstance().getReference().child("students")
//                                .child(getRef(position).getKey()).removeValue();
//                    }
//                });
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//                builder.show();
//            }
//        });
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView firstName, lastName, phoneNumber, email, address;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            firstName = (TextView) itemView.findViewById(R.id.contact_name);
//            lastName = (TextView) itemView.findViewById(R.id.lastName);
            phoneNumber = (TextView) itemView.findViewById(R.id.contact_number);
            email = itemView.findViewById(R.id.contact_email);


//
//            edit = (ImageView) itemView.findViewById(R.id.editicon);
//            delete = (ImageView) itemView.findViewById(R.id.deleteicon);
        }
    }
}
