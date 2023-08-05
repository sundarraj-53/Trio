package com.example.trio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class comment_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText edit;
    Storage store=new Storage();
    Button post;
    String jsonArray;
    ImageButton cancel;
    ArrayList<comment> comments;
    TextView center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView=findViewById(R.id.recycle);
        post=findViewById(R.id.post_comment);
        edit=findViewById(R.id.comment_box);
        comments=new ArrayList<>();
        cancel=findViewById(R.id.back);
        center=findViewById(R.id.comment_center);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try {
            loadData();
        } catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment=edit.getText().toString();
                if(!comment.isEmpty()){
                    try {

                        uploadComment(comment);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void uploadComment(String comment) throws JSONException {
        String url="https://ecapp.onrender.com/api/v1/posts/comments";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("_id",getIntent().getStringExtra("id"));
        jsonObject.put("userId",store.getUser_id());
        jsonObject.put("comment",comment);
        jsonObject.put("userName",store.getName());
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(comment_activity.this, "Hello", Toast.LENGTH_SHORT).show();
                        try {
                            if(response.getString("status").equals("Success")){
                                String username=store.getName();
                                comments.add(new comment(comment,username));
                                commentsBoxAdapter commentsBoxAdapter = new commentsBoxAdapter(comments);
                                recyclerView.setAdapter(commentsBoxAdapter);
                            }
                            else{
                                Toast.makeText(comment_activity.this, "Comment Not Posted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("Volley error",e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(comment_activity.this, "Error Occured due to"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
         {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };

    }

    private void loadData() throws JSONException {
        jsonArray=getIntent().getStringExtra("Comments");
        JSONArray array=new JSONArray(jsonArray);
        Log.d("ARRAY", String.valueOf(array.length()));
        if(array.length()>0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                String comment = json.getString("comment");
                Log.d("ARRAY",comment);
                String username = json.getString("userName");
                Log.d("ARRAY",username);
                comments.add(new comment(comment,username));
                Log.d("ARRAY", String.valueOf(comments));
                commentsBoxAdapter commentsBoxAdapter = new commentsBoxAdapter(comments);
                recyclerView.setAdapter(commentsBoxAdapter);
            }
        }
        else{
            center.setVisibility(View.VISIBLE);
        }

    }
}