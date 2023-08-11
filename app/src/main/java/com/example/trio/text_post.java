package com.example.trio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    Spinner clubs;
    CheckBox check;
    private ProgressBar PB;
    int selectedId;
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
        PB=findViewById(R.id.idPBLoading);
        clubs=findViewById(R.id.spinner_adt);
        back=findViewById(R.id.back);
        check = findViewById(R.id.radio);
        img = findViewById(R.id.tick);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(text_post.this, android.R.layout.simple_spinner_item, store.getArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubs.setAdapter(adapter);
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
        clubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedId=store.club_name.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        PB.setVisibility(View.VISIBLE);
                        sendData(text, Captions,selectedId, tags, mode);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void sendData(String text, String Captions, int selectedId, String tags, boolean mode) throws JSONException {
//        String url = "http://10.11.6.27:3000/api/v1/posts/postDetail";
        String url="https://ecapp.onrender.com/api/v1/posts/postDetail";
        JSONObject json = new JSONObject();
        json.put("text", text);
        json.put("format","text");
        json.put("caption", Captions);
        json.put("club",selectedId);
        json.put("tags", tags);
        json.put("modes", mode);
//        Toast.makeText(text_post.this, "Hii"+store.getKeyUsername(), Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(text_post.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try {
                            if(response.getString("status").equals("Success")){
                                Toast.makeText(text_post.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                        Log.e("Kishore",error.getMessage());
                        Toast.makeText(text_post.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
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