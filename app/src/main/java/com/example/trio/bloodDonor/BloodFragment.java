package com.example.trio.bloodDonor;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
    private ProgressBar PB;
     ViewStub filterview;
     TextView nodata;
     EditText search;
     CheckBox dept,name;
     String selected="";
     public bloodAdapter adapter;
      String[] Filter={"Blood Group","A+","A-","B+","B-","AB+","AB-","O+","O-"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_blood, container, false);
        arrayList=new ArrayList<>();
        filter=view.findViewById(R.id.filter);
        recyle=view.findViewById(R.id.recycle);
        PB=view.findViewById(R.id.idPBLoading);
        dept=view.findViewById(R.id.department_filter);
        name=view.findViewById(R.id.name_filter);
        search=view.findViewById(R.id.searchEt);
        nodata=view.findViewById(R.id.NouserFound);
        ArrayAdapter<String> adt = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Filter);
        adt.setDropDownViewResource(com.github.dhaval2404.imagepicker.R.layout.support_simple_spinner_dropdown_item);
        filter.setAdapter(adt);
        adapter = new bloodAdapter(arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyle.setLayoutManager(linearLayoutManager);
        recyle.setAdapter(adapter);
        PB.setVisibility(View.VISIBLE);
        loadBloodDonor();
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selected=parent.getSelectedItem().toString();
               if(!selected.equals("Blood Group")) {
                   loadBloodData(selected);
               }
               else{
                   loadBloodDonor();
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
//                    adapter.getFilter().filter(s);
                    String searchText = s.toString();
                    makeServerRequest(searchText,selected);
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

    private void loadBloodData(String selected) {
        String url="https://ecapp.onrender.com/api/v1/users/donor?firstName=&bloodGroup="+ Uri.encode(selected);
//        String url="http://10.11.6.27:3000/api/v1/users/donor?firstName=&bloodGroup="+ Uri.encode(selected);;
        Log.d("loadBloodData","MAKE  SERVER");
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("user");
                            int j=response.getInt("result");
                            Log.d("AMMA", String.valueOf(dataObject));
                            arrayList.clear();
                            if(j>0) {
                                nodata.setVisibility(View.GONE);
                                for (int i = 0; i < j; i++) {
                                    JSONObject userObject = dataObject.getJSONObject(i);
                                    Log.d("SUNDAR", String.valueOf(userObject));
                                    String firstN = userObject.getString("firstName");
                                    String lastN = userObject.getString("lastName");
                                    String name = firstN + " " + lastN;
                                    department = userObject.getString("department");
                                    phoneNo = userObject.getString("phoneNo");
                                    blood = userObject.getString("bloodGroup");
                                    String profile = userObject.optString("profileLink", "");
                                    profile=profile.replace("\\/", "/");
                                    if (profile.isEmpty()) {
                                        profile = String.valueOf(R.drawable.baseline_account_circle_24);
                                    }
                                    arrayList.add(new blood(name, department, phoneNo, profile, blood));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                nodata.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (JSONException e)
                        {
                            PB.setVisibility(View.GONE);
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Failed to Connect Server..!", Toast.LENGTH_SHORT).show();
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

    private void makeServerRequest(String searchText,String BloodGroup) {
//        String url="http://10.11.6.27:3000/api/v1/users/donor?firstName="+searchText+"&bloodGroup="+BloodGroup;
        String url="https://ecapp.onrender.com/api/v1/users/donor?firstName="+searchText+"&bloodGroup=";
        Log.d("Make Server","MAKE  SERVER");
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("user");
                            int j=response.getInt("result");
                            Log.d("MARI", String.valueOf(dataObject));
                            arrayList.clear();
                            for (int i=0; i <j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                Log.d("SUNDAR", String.valueOf(userObject));
                                String firstN =userObject.getString("firstName");
                                String lastN = userObject.getString("lastName");
                                String name = firstN + " " + lastN;
                                department = userObject.getString("department");
                                phoneNo = userObject.getString("phoneNo");
                                blood = userObject.getString("bloodGroup");
                                String profile = userObject.optString("profileLink", "");
                                profile=profile.replace("\\/", "/");
                                if (profile.isEmpty()) {
                                    profile = String.valueOf(R.drawable.baseline_account_circle_24);
                                }
                                arrayList.add(new blood(name, department, phoneNo, profile, blood));
                            }
                            adapter.notifyDataSetChanged();
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
                            Toast.makeText(getContext(), "Failed to connect server...!", Toast.LENGTH_SHORT).show();
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

    private void loadBloodDonor() {
//        String url="http://10.11.6.27:3000/api/v1/users/donor?firstName=&department=&bloodGroup=";
        String url="https://ecapp.onrender.com/api/v1/users/donor?firstName=&department=&bloodGroup=";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try {
                            JSONArray dataObject = response.getJSONObject("data").getJSONArray("user");
                            int j=response.getInt("result");
                            Log.d("SUNDAR", String.valueOf(dataObject));
                            arrayList.clear();
                            for (int i=0; i <j; i++) {
                                JSONObject userObject = dataObject.getJSONObject(i);
                                Log.d("SUNDAR", String.valueOf(userObject));
                                String firstN =userObject.getString("firstName");
                                String lastN = userObject.getString("lastName");
                                String name = firstN + " " + lastN;
                                department = userObject.getString("department");
                                phoneNo = userObject.getString("phoneNo");
                                blood = userObject.getString("bloodGroup");
                                String profile = userObject.optString("profileLink", "");
                                profile=profile.replace("\\/", "/");
                                Log.d("PROFILE",profile);
                                if (profile.isEmpty()) {
                                    profile = String.valueOf(R.drawable.baseline_account_circle_24);
                                }
                                arrayList.add(new blood(name, department, phoneNo, profile, blood));
                            }
                            adapter.notifyDataSetChanged();
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
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Failed to connect server...!", Toast.LENGTH_SHORT).show();
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