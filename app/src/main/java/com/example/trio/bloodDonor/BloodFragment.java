package com.example.trio.bloodDonor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.R;
import com.example.trio.Storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BloodFragment extends Fragment {
    ImageView add,filter;
    ViewStub stub;
    RecyclerView recyle;
    Storage store=new Storage();
    private ArrayList<blood> arrayList;
     ViewStub filterview;
     EditText search;
     CheckBox dept,name;
     private bloodAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_blood, container, false);
        arrayList=new ArrayList<>();
        add=view.findViewById(R.id.plusIcon);
        stub=view.findViewById(R.id.dd_ml);
        filter=view.findViewById(R.id.filter);
        recyle=view.findViewById(R.id.recycle);
        filterview=view.findViewById(R.id.filter_view);
        dept=view.findViewById(R.id.department_filter);
        name=view.findViewById(R.id.name_filter);
        search=view.findViewById(R.id.searchEt);
        add.setClickable(true);
        filter.setClickable(true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stub.setVisibility(View.VISIBLE);
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterview.setVisibility(View.VISIBLE);
            }
        });
        loadBloodDonor();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter(s);

                }
                catch (Exception e){
                  e.printStackTrace();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void loadBloodDonor() {
        String url="http://10.11.6.27:3000/api/v1/users/donor?firstName=&department=&bloodGroup=";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject dataObject = response.getJSONObject("data").getJSONObject("user");
                            int j=response.getInt("result");
                            for (int i=0; i <j; i++) {
                                String firstN = dataObject.getString("firstName");
                                String lastN = dataObject.getString("lastName");
                                String name = firstN + " " + lastN;
                                String department = dataObject.getString("department");
                                String phoneNo = dataObject.getString("phoneNo");
                                String blood = dataObject.getString("bloodGroup");
                                String profile = dataObject.optString("image", "");
                                if (profile.isEmpty()) {
                                    profile = String.valueOf(R.drawable.baseline_account_circle_24);
                                }
                                arrayList.add(new blood(name, department, phoneNo, profile, blood));
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