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
import androidx.recyclerview.widget.LinearLayoutManager;
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
    commentsBoxAdapter commentsBoxAdapter;
    String jsonArray;
    ImageButton cancel;
    ArrayList<comment> comments;
    TextView center;

    JSONArray array;

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
        commentsBoxAdapter = new commentsBoxAdapter(comments);
        recyclerView.setAdapter(commentsBoxAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        jsonArray=getIntent().getStringExtra("Comments");
        try {
            array=new JSONArray(jsonArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ;
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
        jsonObject.put("postId",getIntent().getStringExtra("id"));
        jsonObject.put("comment",comment);
        RequestQueue queue= Volley.newRequestQueue(this);
        Log.d("Comment", String.valueOf(jsonObject));
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(comment_activity.this, "Hello", Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE", String.valueOf(response));
                        try {
                            if(response.getString("status").equals("success")){
                                String username = response.getJSONObject("data").getJSONObject("user").getString("userName"); // Handle missing userName
                                String newComment = response.getJSONObject("data").getJSONObject("user").getString("comment");
                                    comments.add(0, new comment(newComment, username));
                                    center.setVisibility(View.GONE);
                                Toast.makeText(comment_activity.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
                                    JSONObject newCommentJson = new JSONObject();
                                    newCommentJson.put("comment", newComment);
                                    newCommentJson.put("userName", username);
                                     array.put(newCommentJson);
                                    Trio trio=new Trio(array);
                                    Log.d("array",response.toString());
                                    Log.d("newarray",array.toString());
                                    commentsBoxAdapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(0);
                                    edit.setText(" ");
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
                        Toast.makeText(comment_activity.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
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
        queue.add(request);

    }

    private void loadData() throws JSONException {

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
            }

        }
        else{
            center.setVisibility(View.VISIBLE);
        }

    }
}