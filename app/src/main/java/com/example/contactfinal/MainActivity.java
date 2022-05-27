package com.example.contactfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;


import com.example.contactfinal.activities.AddContact;
import com.example.contactfinal.adapter.ContactAdapter;
import com.example.contactfinal.adapter.MyViewPagerAdapter;
import com.example.contactfinal.databinding.ActivityMainBinding;
import com.example.contactfinal.model.Contact;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private FloatingActionButton btnAdd;
    private DatabaseReference databaseReference;

    private ActivityMainBinding binding;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setTitle("Contacts");
//
//        recyclerView = findViewById(R.id.recyclerView);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//
//        adapter = new ContactAdapter(options);
//        recyclerView.setAdapter(adapter);
//
//        adapter.startListening();
//        btnAdd = (FloatingActionButton) findViewById(R.id.btn_add);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), AddContact.class));
//            }
//        });
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //create adapter for view pager
        MyViewPagerAdapter adapterViewPage = new MyViewPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(adapterViewPage);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        btnAdd = binding.btnAdd;

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddContact.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Contact");

//        FirebaseRecyclerOptions<Contact> options = new FirebaseRecyclerOptions.Builder<Contact>()
//                .setLifecycleOwner(this)
//                .setQuery(databaseReference,
//                        Contact.class).build();
//        System.out.println("==========key======" + databaseReference.child("Contact").push().getKey());




    }
    @Override
    protected void onStart() {
        super.onStart();
//        adapter.startListening();
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String name) {
        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Contact")
                                .orderByChild("firstName").startAt(name).endAt(name + "\uf8ff"), Contact.class)
                        .build();

        adapter = new ContactAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}