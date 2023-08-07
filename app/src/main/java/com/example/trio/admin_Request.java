package com.example.trio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

public class admin_Request extends Fragment {

    RecyclerView recyclerView;
    TextView name,id;
    TextView center;
    Storage store=new Storage();
    private Context mContext;
    public String Name,dept,club;
    int clubid;
    String userid;
    String userId;
    boolean status;
    int clubId;
    public ArrayList<request> arrayList= new ArrayList<>();;
    Button accept,reject;
    TextView clubName;
    TextView clubSelection;
    private static RequestQueue requestQueue;

    public void setContext(Context context) {
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_admin__request, container, false);
        recyclerView=v.findViewById(R.id.recycle);
        clubName=v.findViewById(R.id.club_name);
        name=v.findViewById(R.id.request_name);
        accept=v.findViewById(R.id.accept_request);
        reject=v.findViewById(R.id.reject_request);
        center=v.findViewById(R.id.no_request);
        clubSelection=v.findViewById(R.id.commitee_selection);
        id=v.findViewById(R.id.userId);
        if(store.getRole()>0){
            clubSelection.setVisibility(View.VISIBLE);
        }
        loadData(getContext());
        clubSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),commitee_activity.class));
            }
        });

        return v;
    }
    public void loadData(Context context) {
//        String url="http://10.11.6.27:3000/api/v1/clubs/request";
        String url="https://ecapp.onrender.com/api/v1/clubs/request";
        JSONObject json=new JSONObject();
        RequestQueue queue=Volley.newRequestQueue(context.getApplicationContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SANTHA", String.valueOf(response));
//                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("SUNDAR", String.valueOf(response));
                            arrayList.clear();
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("users");
                            int j=response.getInt("result");
                            if(j>0){
                                for (int i=0; i <j; i++) {
                                    JSONObject userObject = dataObject.getJSONObject(i);
                                    String first = userObject.getString("firstName");
                                    String last = userObject.getString("lastName");
                                    Name = first + " " + last;
                                    dept = userObject.getString("department");
                                    userid = userObject.getString("userId");
                                    club = userObject.getString("clubName");
                                    //                                userid=Long.parseLong(user);
                                    clubid = userObject.getInt("clubId");
                                    arrayList.add(new request(Name, dept, userid, clubid, club));
                                    if (recyclerView != null) {
                                        adminAdapter Admin = new adminAdapter(getContext(), arrayList);
                                        recyclerView.setAdapter(Admin);
                                    } else {
                                        Log.e("ERROR", "RecyclerView is null.");
                                    }
                                }
                            }
                            else{
                                center.setVisibility(View.VISIBLE);
                            }

                        }
                        catch (JSONException e)
                        {
                            Log.d("SANTHA",e.getMessage());
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
//    public void getValue(String dept_id,boolean b,int clubid) throws JSONException {
//        userId=dept_id;
//        status=b;
//        clubId=clubid;
//
//        sendValue(getContext(),userId,status,clubId);
//        loadData(getContext());
//    }

    public void sendValue(Context context, String dept_id, boolean b, int club_id) throws JSONException {
        Log.d("ASHWIN", String.valueOf(context));
//        String url="http://10.11.6.27:3000/api/v1/clubs/request/"+dept_id+"/"+club_id;

        String url="https://ecapp.onrender.com/api/v1/clubs/request/"+dept_id+"/"+club_id;
        JSONObject json=new JSONObject();
        RequestQueue queue=Volley.newRequestQueue(context);
        json.put("approvalStatus",b);

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSE",response.getString("status"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if(response.getString("status").equals("success")){
                                Log.d("status",response.getString("status"));
                            }
                            else{
                                Log.d("status",response.getString("status"));
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.d("RESPONSE",e.getMessage());
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