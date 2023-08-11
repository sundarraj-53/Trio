package com.example.trio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

public class club_register extends AppCompatActivity {

    Button register;
    Storage store=new Storage();
    private ProgressBar PB;
    CheckBox check;
    ArrayList<Integer> request_club;
    Integer[] array;
    RecyclerView recyclerView;
    club club=new club();
    ArrayList<club> arrayList;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_register);
        register=findViewById(R.id.button);
        request_club= new ArrayList<>();
        PB=findViewById(R.id.idPBLoading);
        back=findViewById(R.id.backbtn);
        arrayList=new ArrayList<club>();
        check=new CheckBox(this);
        recyclerView=findViewById(R.id.recycle);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        PB.setVisibility(View.VISIBLE);
        loadClubData();
//        Toast.makeText(this, "HII"+request_club, Toast.LENGTH_SHORT).show();
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id=check.getId();
                if(isChecked){
                    request_club.add(id);
                    Log.d("CHECK", String.valueOf(id));
                }
                else{
                    request_club.remove(id);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getCheckedClubIds();
                    PB.setVisibility(View.VISIBLE);
                   sendClubData(request_club);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void sendClubData(ArrayList<Integer> club) throws JSONException {
        int array[]=new int[club.size()];
//        String url = "http://10.11.6.27:3000/api/v1/clubs/club";
        String url ="https://ecapp.onrender.com/api/v1/clubs/club";
        JSONObject json = new JSONObject();
        JSONArray uson=new JSONArray();
        for(int i=0;i<club.size();i++){
            int id=club.get(i);
            uson.put(id);
        }
        json.put("clubs",uson);
        RequestQueue queue = Volley.newRequestQueue(club_register.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try {
//                            Toast.makeText(club_register.this, "response" + response, Toast.LENGTH_SHORT).show();
                            String res=response.getString("status");
                            Log.d("response",res);
                            if(res.equals("success")){
                                Toast.makeText(club_register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

//                                onBackPressed();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 300) {
                           onBackPressed();
                        }
                        else{
                            Toast.makeText(club_register.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
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
    private void getCheckedClubIds() {
        int i=0;
        for (club clubs : arrayList) {
            if (clubs.isChecked()) {
                request_club.add(clubs.getId());
                i++;
            }
        }
    }
    private void loadClubData() {
//        String url = "http://10.11.6.27:3000/api/v1/clubs/club";
        String url = "https://ecapp.onrender.com/api/v1/clubs/club";
        JSONObject json = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(club_register.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try {
                            Toast.makeText(club_register.this, "response" + response, Toast.LENGTH_SHORT).show();
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("userNotJoinedClubList");
                            int j = response.getInt("result");
                            Log.d("RAJ", String.valueOf(dataObject));
                            for (int i = 0; i < j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                Log.d("RAJ", String.valueOf(userObject));
                                int id = userObject.getInt("clubId");
                                String clubName=userObject.getString("clubName");
                                arrayList.add(new club(clubName,id));
                                clubAdapter adapter=new clubAdapter(arrayList);
                                recyclerView.setAdapter(adapter);
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
                        PB.setVisibility(View.GONE);
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 300) {
                            startActivity(new Intent(club_register.this, com.example.trio.signUp.register.class));

                        }
                        else{
                            Toast.makeText(club_register.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                Log.d("TOKEN",token);
                headers.put("Authorization","Bearer " + store.getKeyUsername());
                return headers;
            }
        };

        queue.add(request);
    }
}