package com.example.trio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit_profile extends AppCompatActivity {

    EditText first, last, phoneno;
    Spinner department_r;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    public String Phoneno, Department;
    Button submit;
    Storage store = new Storage();
    ImageButton newadd;
    TextView back;
    public int select=-1;
    Uri uri;
    String sImage;
    ArrayAdapter<String> adapter;
    String[] Dept = {"", "IT", "ECE", "EEE", "MECH", "CSE", "CIVIL"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        first = findViewById(R.id.firstname);
        last = findViewById(R.id.lastname);
        phoneno = findViewById(R.id.phoneNoEt);
        department_r = findViewById(R.id.DepartmentEt);
        submit = findViewById(R.id.editProfile);
        newadd=findViewById(R.id.addicon);
        back=findViewById(R.id.back);
        circleImageView=findViewById(R.id.user_profile);
        adapter=setAdapter();
        loadUserDetails();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserDetails();
            }
        });
        newadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Edit_profile.this)
                        .crop()
                        .maxResultSize(1380,1380)
                        .start(101);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK){
            uri=data.getData();
            Context context=Edit_profile.this;
            sImage=RealPathUtil.getRealPath(context,uri);
            Bitmap bitmap= BitmapFactory.decodeFile(sImage);
            circleImageView.setImageBitmap(bitmap);
        }
    }

    private void sendUserDetails()
    {
        String firstName = first.getText().toString();
        String lastName = last.getText().toString();
        String phoneNo = phoneno.getText().toString();
        if(!department_r.getSelectedItem().toString().equals(Department)){
            Department=department_r.getSelectedItem().toString();
        }
        if (!firstName.isEmpty() && !lastName.isEmpty() && !Department.isEmpty() && !phoneNo.isEmpty() )
        {
                try {
                    postMethod(firstName, lastName, Department,phoneNo);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
        }
        else
        {
            if (firstName.isEmpty())
            {
                first.setError("First name should not be empty");
            }
            if (lastName.isEmpty())
            {
                last.setError("Last Name Should not be empty");
            }
            if (phoneNo.isEmpty())
            {
                phoneno.setError("Phone No should Not be entered");
            }
        }
    }
    private void postMethod(String firstName,String lastName,String department,String phoneNo) throws JSONException
    {
//        String url="http://10.11.6.27:3000/api/v1/users/user/updateDetail";
        String url="https://ecapp.onrender.com/api/v1/users/user/updateDetail";
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        JSONObject userDetails =new JSONObject();
        userDetails.put("firstName",firstName);
        userDetails.put("lastName",lastName);
        userDetails.put("department",department);
        userDetails.put("phoneNo",phoneNo);
        JSONObject finaljson=new JSONObject();
        finaljson.put("userDetail",userDetails);
        Log.d("NAGRAJAN", String.valueOf(userDetails));
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.PATCH, url,finaljson,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.d("JSONOBJECT","HII");
                            String res=response.getString("status");
                            if(res.equals("success")){
                               onBackPressed();
                            }
                        }
                        catch (JSONException e){
                            Toast.makeText(Edit_profile.this, "Method Exception Occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Edit_profile.this, "Error Occured "+error, Toast.LENGTH_SHORT).show();
                    }
                }){
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
    public ArrayAdapter<String> setAdapter()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Edit_profile.this, android.R.layout.simple_spinner_dropdown_item, Dept);
        department_r.setAdapter(adapter);
        return adapter;
    }
    private void loadUserDetails()
    {
//        String url="http://10.11.6.27:3000/api/v1/users/user";
        String url="https://ecapp.onrender.com/api/v1/users/user";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        ArrayList subt=new ArrayList();
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response=response.getJSONObject("data").getJSONObject("user");
                            Log.d("ASHWIN", String.valueOf(response));
                            String firstN =response.getString("firstName");
                            String lastN = response.getString("lastName");
                            Department = response.getString("department");
                            Phoneno = response.getString("phoneNo");
                            select=adapter.getPosition(Department);
                            first.setText(firstN);
                            last.setText(lastN);
                            phoneno.setText(Phoneno);
                            if(select>0){
                                department_r.setSelection(select);
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

                            Toast.makeText(Edit_profile.this, "Volley Error", Toast.LENGTH_SHORT).show();
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