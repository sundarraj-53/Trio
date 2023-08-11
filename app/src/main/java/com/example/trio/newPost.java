package com.example.trio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


public class newPost extends Fragment {


    androidx.cardview.widget.CardView cardView;
   AppCompatButton btn1,btn2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_new_post, container, false);
        btn1=view.findViewById(R.id.media);
        btn2=view.findViewById(R.id.text);
        cardView=view.findViewById(R.id.card_post);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getContext(),image_post.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),text_post.class));
            }
        });
        return view;
    }
}