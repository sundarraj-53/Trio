package com.example.trio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class text_post extends AppCompatActivity {

    EditText text_msg, captions, tag;
    CheckBox check;
    boolean mode = false;
    Storage store = new Storage();
    ImageButton img,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);
        text_msg = findViewById(R.id.text_edit);
        captions = findViewById(R.id.captions_edit);
        tag = findViewById(R.id.text_tag);
        back=findViewById(R.id.back);
        check = findViewById(R.id.radio);
        img = findViewById(R.id.tick);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mode = true;
                } else {
                    mode = false;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = text_msg.getText().toString();
                Log.d("text_post",text);
                String Captions = captions.getText().toString();
                Log.d("Captions",Captions);
                String tags = tag.getText().toString();
                Log.d("tags",tags);
                if (text != null) {
                    try {
                        sendData(text, Captions, tags, mode);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void sendData(String text, String Captions, String tags, boolean mode) throws JSONException {
        String url = "http://10.11.6.27:3000/api/v1/posts/postDetail";
        JSONObject json = new JSONObject();
        json.put("text", text);
        json.put("format","text");
        json.put("caption", Captions);
        json.put("club",2);
        json.put("tags", tags);
        json.put("modes", mode);
        Toast.makeText(text_post.this, "Hii"+store.getKeyUsername(), Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(text_post.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("Success")){
                                startActivity(new Intent(text_post.this,UserFragment.class));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Kishore",error.getMessage());
                        Toast.makeText(text_post.this, "Volley Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token =store.getKeyUsername();
                Log.d("TOKEN",token);
//                Toast.makeText(text_post.this, "Token"+token, Toast.LENGTH_SHORT).show();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(request);

    }
}