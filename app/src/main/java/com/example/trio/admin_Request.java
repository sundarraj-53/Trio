package com.example.trio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class admin_Request extends Fragment {

    RecyclerView recyclerView;
    TextView name,id;
    Storage store=new Storage();
    public String Name,dept,userid;
    public ArrayList<request> arrayList;
    Button accept,reject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_admin__request, container, false);
        recyclerView=v.findViewById(R.id.recycle);
        arrayList=new ArrayList<>();
        name=v.findViewById(R.id.request_name);
        accept=v.findViewById(R.id.accept_request);
        reject=v.findViewById(R.id.reject_request);
        id=v.findViewById(R.id.userId);
        loadData();
        return v;
    }
    private void loadData() {
        String url="http://10.11.6.27:3000/api/v1/users/request";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SANTHA", String.valueOf(response));
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("users");
                            int j=response.getInt("result");
                            for (int i=0; i <j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                String first=userObject.getString("firstName");
                                String last=userObject.getString("lastName");
                                Name=first+" "+last;
                                dept=userObject.getString("department");
                                userid=userObject.getString("userId");
                                arrayList.add(new request(getContext(),Name,dept,userid));
                                adminAdapter Admin=new adminAdapter(arrayList);
                                recyclerView.setAdapter(Admin);
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
}