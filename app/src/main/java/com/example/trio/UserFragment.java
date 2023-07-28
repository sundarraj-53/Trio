package com.example.trio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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


public class UserFragment extends Fragment {


    TextView name,email,department,phoneno,edit;
    Storage store=new Storage();
    public String Name,Phoneno,Department,Email,Profile;
    de.hdodenhof.circleimageview.CircleImageView profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        name=view.findViewById(R.id.profileName);
        email=view.findViewById(R.id.profileEmail);
        department=view.findViewById(R.id.profiledepartment);
        phoneno=view.findViewById(R.id.profilephone);
        edit=view.findViewById(R.id.edit_text);
        profile=view.findViewById(R.id.user_profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(),));
            }
        });
        loadUserDetails();
        return view;
    }

    private void loadUserDetails() {
        String url="http://10.11.6.27:3000/api/v1/users/user";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(getContext(), "Hii "+response, Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("user");
//                            JSONArray array=dataObject.getJSONArray("value");
                            int j=response.getInt("result");
                            Log.d("SUNDAR", String.valueOf(dataObject));
                            for (int i=0; i <j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                Log.d("SUNDAR", String.valueOf(userObject));
                                String firstN =userObject.getString("firstName");
                                String lastN = userObject.getString("lastName");
                                Name = firstN + " " + lastN;
                                Department = userObject.getString("department");
                                Phoneno = userObject.getString("phoneNo");
                                Email=userObject.getString("email");
                                Profile = userObject.optString("image", "");
                                if (Profile.isEmpty()) {
                                    Profile = String.valueOf(R.drawable.baseline_account_circle_24);
                                }
                            }
                            name.setText(Name);
                            department.setText(Department);
                            email.setText(Email);
                            phoneno.setText(Phoneno);
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