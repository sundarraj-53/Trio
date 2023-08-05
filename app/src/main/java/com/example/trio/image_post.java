package com.example.trio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class image_post extends AppCompatActivity {

    ImageButton back,post;
    ImageView view;
    ImageButton add;
    Storage store=new Storage();
    EditText captions,tag;
    byte[] base64Data;
    CheckBox check;
    boolean mode=false;
    String path;
    Uri uri;
//    File bitmap;
    File file;
    String sImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);
        back=findViewById(R.id.backbtn);
        view=findViewById(R.id.image);
        add=findViewById(R.id.newPost);
        captions=findViewById(R.id.edit);
        tag=findViewById(R.id.edit_tag);
        check=findViewById(R.id.radio1);
        post=findViewById(R.id.tick);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Captions=captions.getText().toString();
                String tags=tag.getText().toString();
                try {
                    uploadImage(file,Captions,tags,mode);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
    }

    private void uploadImage(File bitmap, String captions, String tags, boolean mode) throws JSONException {
        String url="http://10.11.6.27:3000/api/v1/posts/apppost";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("captions", captions);
        jsonObject.put("tags", tags);
        jsonObject.put("modes", mode);
        VolleyMultipartRequest volleyMultipartRequest =new VolleyMultipartRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MULTIPART", "Response:Hii " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MULTIPART", "Error: " + error.getMessage());
                    }
                },
                bitmap,
                captions,
                tags,
                mode,
                jsonObject);
        RequestQueue requestQueue = Volley.newRequestQueue(image_post.this);
        requestQueue.add(volleyMultipartRequest);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK){
            uri=data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                file =new File(uri.getPath());
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