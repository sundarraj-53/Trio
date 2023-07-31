package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;
import com.example.trio.signUp.register;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit_profile extends AppCompatActivity {

    EditText first, last, phoneno, Password, email;
    Spinner department_r, blood;
    public String pass, Phoneno, Department, Email;
    RelativeLayout re;
    AppCompatImageView un;

    int group;
    Button submit;
    Storage store = new Storage();
    String[] Dept = {"", "IT", "ECE", "EEE", "MECH", "CSE", "CIVIL"};
    String[] bloodgroup = {"", "B+", "B-", "A+", "A-", "O+", "O-", "AB+", "AB-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        first = findViewById(R.id.firstname);
        last = findViewById(R.id.lastname);
        email = findViewById(R.id.emailEt);
        un=findViewById(R.id.dummy);
        Password = findViewById(R.id.pass);
        phoneno = findViewById(R.id.phoneNoEt);
        department_r = findViewById(R.id.DepartmentEt);
        blood = findViewById(R.id.bloodEt);
        re = findViewById(R.id.visible);
        submit = findViewById(R.id.editProfile);
        setAdapter();
        loadUserDetails();
        setAdapter();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserDetails();
            }
        });

    }

    private void sendUserDetails() {
        String firstName = first.getText().toString();
        String lastName = last.getText().toString();
        String department = department_r.getSelectedItem().toString();
        String password = Password.getText().toString();
        String phoneNo = phoneno.getText().toString();
        String bloodGroup = blood.getSelectedItem().toString();
        if (!firstName.isEmpty() && !lastName.isEmpty() && !department.isEmpty() && !password.isEmpty() && !phoneNo.isEmpty() && bloodGroup.isEmpty() || !bloodGroup.isEmpty()) {
            if (isValidPassword(password)) {
                try {
                    postMethod(email, firstName, lastName, department, password, phoneNo, bloodGroup);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Password.setError("Your Password is Not Strong");
            }
        } else {
            if (firstName.isEmpty()) {
                first.setError("First name should not be empty");
            }
            if (lastName.isEmpty()) {
                last.setError("Last Name Should not be empty");
            }
            if (password.isEmpty()) {
                Password.setError("Password Should Not be entered");
            }
            if (phoneNo.isEmpty()) {
                phoneno.setError("Phone No should Not be entered");
            }
        }
    }

    private void postMethod(EditText email, String firstName, String lastName, String department, String password, String phoneNo, String bloodGroup) throws JSONException {
        String url="http://10.11.6.27:3000/api/v1/users/user";
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        JSONObject json=new JSONObject();
        json.put("email",email);
        json.put("firstName",firstName);
        json.put("lastName",lastName);
        json.put("department",department);
        json.put("password",password);
        json.put("phoneNo",phoneNo);
        json.put("bloodGroup",bloodGroup);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.PATCH, url,json,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String res=response.getString("status");
                            if(res.equals("success")){
                                startActivity(new Intent(Edit_profile.this, UserFragment.class));
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
                });
        queue.add(request);

    }


    public void setAdapter() {
        ArrayAdapter<String> adt = new ArrayAdapter<>(Edit_profile.this, android.R.layout.simple_spinner_dropdown_item, bloodgroup);
        blood.setAdapter(adt);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Edit_profile.this, android.R.layout.simple_spinner_dropdown_item, Dept);
        department_r.setAdapter(adapter);
    }
    private void loadUserDetails() {
        String url="http://10.11.6.27:3000/api/v1/users/user";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        ArrayList add=new ArrayList();
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
                            pass=response.getString("password");
                            Email=response.getString("email");
                            subt.add(Department);
                            boolean be=response.getBoolean("bloodDonor");
                            if(true) {
                                re.setVisibility(View.VISIBLE);
                                un.setVisibility(View.VISIBLE);
                                group= Integer.parseInt(response.getString("bloodGroup"));
                                add.add(group);
                            }
                            else {
                                re.setVisibility(View.GONE);
                            }
                            first.setText(firstN);
                            last.setText(lastN);
                            department_r.setAdapter((SpinnerAdapter) subt);
                            email.setText(Email);
                            phoneno.setText(Phoneno);
                            Password.setText(pass);
                            blood.setAdapter((SpinnerAdapter) add);
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
                        if (Edit_profile.this != null) {
                            Toast.makeText(Edit_profile.this, "Volley Error", Toast.LENGTH_SHORT).show();
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
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\p{Punct}]{8,}$";;
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}