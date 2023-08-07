package com.example.trio;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AdminWork extends Fragment {
    RecyclerView recyclerView;
    Storage store=new Storage();
    adminViewAdapter adminAdapter;
    AdminWork adminWork;

    ArrayList<AdminHolder> viewList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_admin_work, container, false);
        recyclerView=view.findViewById(R.id.recycle);
        adminWork=new AdminWork();
        viewList=new ArrayList<AdminHolder>();
        adminAdapter = new adminViewAdapter(getContext(), viewList,adminWork);
        recyclerView.setAdapter(adminAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        loadDetails();
        return view;
    }

    private void loadDetails() {
        String url = "https://ecapp.onrender.com/api/v1/users/request";
        JSONObject json = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONObject("data").getJSONArray("eligibleToJoinAsAdminUsersList");
                            Log.d("ADMIN SELECTION", String.valueOf(array));
                            int j=response.getInt("result");
                            for(int i=0;i<j;i++){
                                ArrayList<String> clubName=new ArrayList<>();
                                clubName.add("Select club");
                                ArrayList<Integer>clubId=new ArrayList<>();
                                clubId.add(0);
                                JSONObject newjson=array.getJSONObject(i);
                                String userId=newjson.getString("userId");
                                Log.d("ADMIN SELECTION",userId);
                                String first=newjson.getString("firstName");
                                String last=newjson.getString("lastName");
                                String name=first+" "+last;
                                Log.d("ADMIN SELECTION",name);
                                String department=newjson.getString("department");
                                Log.d("ADMIN SELECTION",department);
                                JSONArray rew=newjson.getJSONArray("clubs");
                                for(int k=0;k<rew.length();k++){
                                    JSONObject jio=rew.getJSONObject(k);
                                    clubId.add(jio.getInt("clubId"));
                                    clubName.add(jio.getString("clubName"));
                                }
                                Log.d("ADMIN SELECTION",clubId+" "+clubName);
                                viewList.add(new AdminHolder(getContext(),userId,name,department,clubId,clubName));
                            }
                            adminAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
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

    public void sendData(Context context, String userId, int[] selectedId) throws JSONException {
        int id=selectedId[0];
        Log.d("JACK",id+" "+userId);
        String url="https://ecapp.onrender.com/api/v1/users/request/"+userId+"/"+id;
        Log.d("JACK",url);
        JSONObject json = new JSONObject();
        json.put("approvalStatus",true);
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.d("JACK","REQUEST IS READY");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("success")){
                                Toast.makeText(context, "User Added  Successfully", Toast.LENGTH_SHORT).show();
                                loadDetails();
                            }
                            else{
                                Toast.makeText(context, "User Not added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("REVIEW", error.getMessage());
                        if (getContext() != null) {
                            Toast.makeText(context, "Volley Error", Toast.LENGTH_SHORT).show();
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