package com.example.trio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

public class Trio_Home extends Fragment {


    private RecyclerView recyclerView;
    Storage store=new Storage();
    Spinner clubs;

    private int count=0;
    private int like;
    int selectedId=0;

    TextView cnd,captions,club_name,tag;
    ImageView adminimg;
    ImageButton cmnd;
    TextView noPOst;
    private ArrayList<Trio> arrayList;
    private Context context;

    private RecyclerAdapter recyclerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trio__home, container, false);
        arrayList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.reycler_view);
        clubs=view.findViewById(R.id.spinner_adt);
        cnd=view.findViewById(R.id.count);
        adminimg=view.findViewById(R.id.selectionadmin);
        noPOst=view.findViewById(R.id.noPost);
        cmnd=view.findViewById(R.id.comment);
        captions=view.findViewById(R.id.description);
        club_name=view.findViewById(R.id.club_name);
        recyclerAdapter = new RecyclerAdapter(context, arrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        Toast.makeText(context, "Hii"+store.getMember(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, store.getHomeFilter());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubs.setAdapter(adapter);
        tag=view.findViewById(R.id.tag);
        Toast.makeText(context, "Hii"+store.getRole(), Toast.LENGTH_SHORT).show();
        if(store.getRole()!=0){
            adminimg.setVisibility(View.VISIBLE);
        }
        adminimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), post.class));
            }
        });
        clubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedId=store.clubIDNames.get(position);
                Toast.makeText(getContext(), "Hii"+selectedId, Toast.LENGTH_SHORT).show();
                if(selectedId!=0) {
                    sendData(selectedId);
                }
                else{
                    loadPostDetails();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void sendData(int selectedId) {
        String url="http://10.11.6.27:3000/api/v1/posts?clubId="+selectedId;
//        String url="https://ecapp.onrender.com/api/v1/posts?clubId="+selectedId;
        Log.d("BOTTLE", String.valueOf(selectedId));
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FINEARTS", String.valueOf(response));
                        try {
                            arrayList.clear();
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("posts");
                            int j=response.getInt("result");
                            if(j>0) {
                                Log.d("SUNDAR", String.valueOf(dataObject));
                                for (int i = 0; i < j; i++) {
                                    JSONObject userObject = dataObject.getJSONObject(i);
                                    Log.d("SUNDAR", String.valueOf(userObject));
//                                String profile=userObject.getString("profileIcon");
                                    String format = userObject.getString("format");
                                    if (format.equals("image")) {
                                        String image = userObject.getString("image");
                                        image = image.replace("\\/", "/");
                                        String clubimage=userObject.getString("clubLogo");
                                        clubimage = clubimage.replace("\\/", "/");
                                        Log.d("SUNDAR", image);
                                        JSONArray comment = userObject.getJSONArray("comments");
                                        Log.d("COMMENTS", String.valueOf(comment));
                                        String caption = userObject.getString("caption");
                                        String id = userObject.getString("_id");
                                        String clubName = userObject.getString("clubName");
                                        String tag = userObject.getString("tags");
                                        String like = String.valueOf(userObject.getInt("likes"));
//                                    ArrayList<Trio> arrayList=new ArrayList<>();
                                        arrayList.add(new Trio(id, format, image, tag, caption, clubName, like, "", comment,clubimage));
                                    } else {
                                        String caption = userObject.getString("caption");
                                        String clubName = userObject.getString("clubName");
                                        String tag = userObject.getString("tags");
                                        String clubimage=userObject.getString("clubLogo");
                                        clubimage = clubimage.replace("\\/", "/");
                                        String text = userObject.getString("text");
                                        JSONArray comment = userObject.getJSONArray("comments");
                                        Log.d("SUBRAMANIYAN", text + " " + tag);
                                        String like = String.valueOf(userObject.getInt("likes"));
                                        String id = userObject.getString("_id");
                                        arrayList.add(new Trio(id, format, "", tag, caption, clubName, like, text, comment, clubimage));
                                    }
                                }
                                recyclerAdapter.notifyDataSetChanged();
                            }
                            else{
                                noPOst.setVisibility(View.VISIBLE);
                                recyclerAdapter.notifyDataSetChanged();
                                return;
                              }

                           } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };

        queue.add(request);
    }
    private void loadPostDetails() {
        String url="http://10.11.6.27:3000/api/v1/posts?clubId=";
//        String url="https://ecapp.onrender.com/api/v1/posts?clubId=";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        arrayList.clear();
                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("posts");
                            int j=response.getInt("result");
                            Log.d("SUNDAR", String.valueOf(dataObject));
                            for (int i=0; i <j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                Log.d("SUNDAR", String.valueOf(userObject));
//                                String profile=userObject.getString("profileIcon");
                                String format=userObject.getString("format");
                                if(format.equals("image")) {
                                    String image = userObject.getString("image");
                                    image= image.replace("\\/", "/");
//                                    image=image.replace("http","https");
                                    String clubimage=userObject.getString("clubLogo");
                                    clubimage = clubimage.replace("\\/", "/");
                                    Log.d("SUNDAR",image);
                                    Log.d("CLUBIMAGE",clubimage);
                                    JSONArray comment=userObject.getJSONArray("comments");
                                    Log.d("COMMENTS", String.valueOf(comment));
                                    String caption = userObject.getString("caption");
                                    String id=userObject.getString("_id");
                                    String clubName = userObject.getString("clubName");
                                    String tag = userObject.getString("tags");
                                    String like = String.valueOf(userObject.getInt("likes"));
//                                    ArrayList<Trio> arrayList=new ArrayList<>();
                                    arrayList.add(new Trio(id,format,image, tag, caption,clubName,like,"",comment, clubimage));
                                }
                                else{
                                    String caption = userObject.getString("caption");
                                    String clubName = userObject.getString("clubName");
                                    String clubimage=userObject.getString("clubLogo");
                                    clubimage = clubimage.replace("\\/", "/");
                                    String tag = userObject.getString("tags");
                                    String text= userObject.getString("text");
                                    JSONArray comment=userObject.getJSONArray("comments");
                                    Log.d("SUBRAMANIYAN",text+" "+tag);
                                    String like = String.valueOf(userObject.getInt("likes"));
                                    String id=userObject.getString("_id");
                                    arrayList.add(new Trio(id,format,"",tag, caption, clubName,like,text,comment, clubimage));
                                }
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Failed to Connect Server..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };

        queue.add(request);
    }
    public void sendLikeData(Context context, String id, boolean b) throws JSONException {
        String url="https://ecapp.onrender.com/api/v1/posts/like";
        JSONObject json=new JSONObject();
        json.put("postId",id);
        json.put("like",b);
        RequestQueue queue=Volley.newRequestQueue(context);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getString("status").equals("success")){
                                Log.d("HEART","YOUR LIKE IS ADDED/REMOVED");
//                                Toast.makeText(context, "YOUR LIKE IS ADDED", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Log.d("HEART","YOUR LIKE IS NOT ADDED/REMOVED");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Failed to Connect Server..!", Toast.LENGTH_SHORT).show();
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
}