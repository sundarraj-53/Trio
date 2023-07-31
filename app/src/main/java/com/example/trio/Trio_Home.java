package com.example.trio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;
import com.example.trio.bloodDonor.blood;
import com.example.trio.bloodDonor.bloodAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trio_Home extends Fragment {


    private RecyclerView recyclerView;
    Storage store=new Storage();
    private ArrayList<Trio> arrayList;
    private int count=0;
    private int like;

    TextView cnd;
    private boolean isImageButtonChecked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trio__home, container, false);
        arrayList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.reycler_view);
        cnd=view.findViewById(R.id.count);
        loadPostDetails();
        return view;
    }

    private void loadPostDetails() {
        String url="";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("user");
                            int j=response.getInt("result");
                            Log.d("SUNDAR", String.valueOf(dataObject));
                            for (int i=0; i <j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                String profile=userObject.getString("profileIcon");
                                String image=userObject.getString("Image");
                                String caption=userObject.getString("captions");
                                String clubName=userObject.getString("clubName");
                                String like=userObject.getString("like");

                                arrayList.add(new Trio(profile,image,caption,clubName,like));
                                RecyclerAdapter recyclerAdapter=new RecyclerAdapter(arrayList);
                                recyclerView.setAdapter(recyclerAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
}