package com.example.trio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserFragment extends Fragment {


    TextView name,email,department,phoneno,signout,register;
    Button edit;
    Storage store=new Storage();
    private ProgressBar PB;
    public String Name,Phoneno,Department,Email,Profile;
    de.hdodenhof.circleimageview.CircleImageView profile;

    private Context context;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        name=view.findViewById(R.id.profileName);
        PB=view.findViewById(R.id.idPBLoading);
        email=view.findViewById(R.id.profileEmail);
        department=view.findViewById(R.id.profiledepartment);
        phoneno=view.findViewById(R.id.profilephone);
        signout=view.findViewById(R.id.sign_out);
        edit=view.findViewById(R.id.edit_text);
        register=view.findViewById(R.id.register);
        profile=view.findViewById(R.id.user_profile);
        PB.setVisibility(View.VISIBLE);
        if(store.getAdmin().equals("true")){
            register.setVisibility(View.GONE);
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.deleteUsername();
                startActivity(new Intent(getContext(),MainActivity.class));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Edit_profile.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),club_register.class));
            }
        });
        loadUserDetails();
        return view;
    }

    private void loadUserDetails() {
//        String url="http://10.11.6.27:3000/api/v1/users/user";
        String url="https://ecapp.onrender.com/api/v1/users/user";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);

                        try {
                            response=response.getJSONObject("data").getJSONObject("user");
                            Log.d("ASHWIN", String.valueOf(response));
                                store.saveId(response.getString("_id"));
                                String firstN =response.getString("firstName");
                                String lastN = response.getString("lastName");
                                Name = firstN + " " + lastN;
                                Department = response.getString("department");
                                Phoneno = response.getString("phoneNo");
                                Email=response.getString("email");
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
                        PB.setVisibility(View.GONE);
                        Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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