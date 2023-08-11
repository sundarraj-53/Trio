package com.example.trio;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    private ProgressBar PB;
    AdminWork adminWork;

    ArrayList<AdminHolder> viewList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_admin_work, container, false);
        recyclerView=view.findViewById(R.id.recycle);
        PB=view.findViewById(R.id.idPBLoading);
        adminWork=new AdminWork();
        viewList=new ArrayList<AdminHolder>();
        adminAdapter = new adminViewAdapter(getContext(), viewList,adminWork);
        recyclerView.setAdapter(adminAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        PB.setVisibility(View.VISIBLE);
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
                        PB.setVisibility(View.GONE);
                        try {
                            JSONArray array=response.getJSONObject("data").getJSONArray("eligibleToJoinAsAdminUsersList");
                            Log.d("SELVAM", String.valueOf(array));
                            int j=response.getInt("result");
                            Log.d("SELVAM",String.valueOf(j));
                            for(int i=0;i<j;i++){
                                ArrayList<String> clubName=new ArrayList<>();
                                clubName.add("Select club");
                                ArrayList<Integer>clubId=new ArrayList<>();
                                clubId.add(0);
                                JSONObject newjson=array.getJSONObject(i);
                                Log.d("SELVAM",newjson.toString());
                                String userId=newjson.getString("userId");
                                Log.d("ADMIN SELECTION",userId);
                                String firstName = newjson.optString("firstName", "");
                                String lastName = newjson.optString("lastName", "");
                                String name=firstName+" "+lastName;
                                Log.d("NAME",name);
//                                Log.d("SELVAM",name);
                                String department=newjson.getString("department");
                                String email=newjson.getString("email");
                                Log.d("ADMIN SELECTION",department);
                                JSONArray rew=newjson.getJSONArray("clubs");
                                for(int k=0;k<rew.length();k++){
                                    JSONObject jio=rew.getJSONObject(k);
                                    clubId.add(jio.getInt("clubId"));
                                    clubName.add(jio.getString("clubName"));
                                }
                                Log.d("ADMIN SELECTION",clubId+" "+clubName);
                                viewList.add(new AdminHolder(getContext(),userId,name,department,clubId,clubName,email));
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
                        PB.setVisibility(View.GONE);
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
        PB.setVisibility(View.VISIBLE);
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
                        PB.setVisibility(View.GONE);
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
                        PB.setVisibility(View.GONE);
                        if (getContext() != null) {
                            Toast.makeText(context, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
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