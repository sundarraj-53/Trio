package com.example.trio.bloodDonor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.trio.R;
import com.example.trio.Storage.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BloodFragment extends Fragment {
    Spinner filter;
    RecyclerView recyle;
    Storage store=new Storage();
    private ArrayList<blood> arrayList;
    public String department,phoneNo,blood;
     ViewStub filterview;
     EditText search;
     CheckBox dept,name;
     public bloodAdapter adapter;
      String[] Filter={"","NAME","DEPARTMENT"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_blood, container, false);
        arrayList=new ArrayList<>();
        filter=view.findViewById(R.id.filter);
        recyle=view.findViewById(R.id.recycle);
//        filterview=view.findViewById(R.id.filter_view);
        dept=view.findViewById(R.id.department_filter);
        name=view.findViewById(R.id.name_filter);
        search=view.findViewById(R.id.searchEt);
        ArrayAdapter<String> adt = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Filter);
        filter.setAdapter(adt);
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
                                String name = firstN + " " + lastN;
                                department = userObject.getString("department");
                                phoneNo = userObject.getString("phoneNo");
                                blood = userObject.getString("bloodGroup");
                                String profile = userObject.optString("image", "");
                                if (profile.isEmpty()) {
                                    profile = String.valueOf(R.drawable.baseline_account_circle_24);
                                }
                                arrayList.add(new blood(name, department, phoneNo, profile, blood));
                                adapter=new bloodAdapter(arrayList);
                                recyle.setAdapter(adapter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                recyle.setLayoutManager(linearLayoutManager);
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