package com.example.trio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trio.Storage.Storage;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class image_post extends AppCompatActivity {

    ImageButton back,post;
    ImageView view;
    ImageButton add;
    EditText captions,tag;
    byte[] base64Data;
    CheckBox check;
    TextView Change;
    Bitmap bitmap;
    boolean mode=false;
    Storage store=new Storage();
    String path;
    Uri uri;
    File IMage;
    String sImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);
        back=findViewById(R.id.backbtn);
        view=findViewById(R.id.image);
        add=findViewById(R.id.newPost);
        Change=findViewById(R.id.change);
        captions=findViewById(R.id.edit);
        tag=findViewById(R.id.edit_tag);
        check=findViewById(R.id.radio1);
        post=findViewById(R.id.tick);
        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(image_post.this)
                        .crop()
                        .maxResultSize(1380,1380)
                        .start(101);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add.setVisibility(View.GONE);
                ImagePicker.Companion.with(image_post.this)
                    .crop()
                        .maxResultSize(1380,1380)
                        .start(101);
            }
        });
        if(uri==null){
            add.setVisibility(View.VISIBLE);
        }
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mode=true;
                }
                else{
                    mode=false;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                try {
                    Toast.makeText(image_post.this, "HIi"+uri, Toast.LENGTH_SHORT).show();
                    String caption=captions.getText().toString();
                    String tags=tag.getText().toString();
//                    sendData(uri,caption,tags,mode);
//                }
//                catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }

            }
        });
    }

    private void sendData(byte[] base64Data, String caption, String tags, boolean mode) {
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK){
            uri=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    base64Data = Base64.getEncoder().encode(bytes);
                }
                sImage=new String(base64Data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            view.setImageURI(uri);
        }
        else{
            Toast.makeText(this, "No Image Selected...!", Toast.LENGTH_SHORT).show();
        }
    }
}