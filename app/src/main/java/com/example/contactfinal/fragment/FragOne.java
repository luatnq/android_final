package com.example.contactfinal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contactfinal.R;


public class FragOne extends Fragment {
    private int mPage;
    private String mTitle;
    private TextView mText;
    public static Fragment newInstance(int page, String title) {
        FragOne fragOneFrag = new FragOne();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        fragOneFrag.setArguments(args);
        return fragOneFrag;
    }

    //show hello world
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("page", 0);
        mTitle = getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragone, container, false);
        mText = (TextView) rootView.findViewById(R.id.textView);
        //set text
        mText.setText("hello worlddddddddddddd");


        return rootView;
    }


}
