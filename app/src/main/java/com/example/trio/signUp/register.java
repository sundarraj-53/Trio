package com.example.trio.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.MainActivity;
import com.example.trio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    Intent i;
    String email, role;
    private ProgressBar PB;
    TextView title;
    EditText fname, lname, Password, confirmpassword, phoneno;
    Spinner Department, blood;
    CheckBox check;
    Button Submit;
    String[] Dept = {"", "IT", "ECE", "EEE", "MECH", "CSE", "CIVIL"};
    String[] bloodgroup = {"", "B+", "B-", "A+", "A-", "O+", "O-", "AB+", "AB-"};
    String bloodGroup;
    TextView bloodgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        title = findViewById(R.id.registeruser_title);
        fname = findViewById(R.id.Fname_registeruser_student);
        lname = findViewById(R.id.Lname_registeruser_student);
        PB=findViewById(R.id.idPBLoading);
        Department = findViewById(R.id.Department_registeruser_student);
        Password = findViewById(R.id.password_registeruser_student);
        confirmpassword = findViewById(R.id.conpassword_registeruser_student);
        phoneno = findViewById(R.id.phoneno_registeruser_student);
        check = findViewById(R.id.stud_checked);
        blood = findViewById(R.id.blooddonor_ml);
        Submit = findViewById(R.id.submit);
        bloodgr=findViewById(R.id.bloodgr);
        ArrayAdapter<String> adt = new ArrayAdapter<>(register.this, android.R.layout.simple_spinner_dropdown_item, bloodgroup);
        blood.setAdapter(adt);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_dropdown_item, Dept);
        Department.setAdapter(adapter);
        i = getIntent();
        email = i.getStringExtra("Email");
        role = i.getStringExtra("type");
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bloodgr.setVisibility(View.VISIBLE);
                    blood.setVisibility(View.VISIBLE);
                } else {
                    blood.setVisibility(View.GONE);
                    bloodgr.setVisibility(View.GONE);
                }
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String department = Department.getSelectedItem().toString();
                String password = Password.getText().toString();
                String confirmPassword = confirmpassword.getText().toString();
                String phoneNo = phoneno.getText().toString();
                String bloodGroup = blood.getSelectedItem().toString();
//                Toast.makeText(register.this, "BLood Group"+bloodGroup, Toast.LENGTH_SHORT).show();
                if (!firstName.isEmpty() && !lastName.isEmpty() && !department.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !phoneNo.isEmpty() && bloodGroup.isEmpty() || !bloodGroup.isEmpty()) {
                    if (isValidPassword(password)) {
                        if (password.equals(confirmPassword)) {
                            try {
                                PB.setVisibility(View.VISIBLE);
                                postMethod(role, email, firstName, lastName, department, password,phoneNo,bloodGroup);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            confirmpassword.setError("Password and Confirm Password is Different");
                        }
                    } else {
                        Password.setError("Your Password is Not Strong");
                    }
                } else {
                    if (firstName.isEmpty()) {
                        fname.setError("First name should not be empty");
                    }
                    if (lastName.isEmpty()) {
                        lname.setError("Last Name Should not be empty");
                    }
                    if (password.isEmpty()) {
                        Password.setError("Password Should Not be entered");
                    }
                    if (confirmPassword.isEmpty()) {
                        confirmpassword.setError("Confirm Password Should Not be entered");
                    }
                    if (phoneNo.isEmpty()) {
                        phoneno.setError("Phone No should Not be entered");
                    }
                }
            }
        });
    }


    private void postMethod(String role, String email, String firstName, String lastName, String department, String password,String phoneNo, String bloodGroup) throws JSONException {
//        String url="http://10.11.6.27:3000/api/v1/users/user";
        String url="https://ecapp.onrender.com/api/v1/users/user";
        RequestQueue queue= Volley.newRequestQueue(register.this);
        JSONObject json=new JSONObject();
        json.put("role",role);
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
                PB.setVisibility(View.GONE);
                try{
                    String res=response.getString("status");
                    if(res.equals("success")){
                        startActivity(new Intent(register.this, MainActivity.class));
                    }
                    if(res.equals("error")){
                        startActivity(new Intent(register.this,MainActivity.class));
                    }

                }
                catch (JSONException e){
                    Toast.makeText(register.this, "Error Occured due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
        new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                PB.setVisibility(View.GONE);
                error.printStackTrace();
                Toast.makeText(register.this, "Failed to connect to server..!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);


    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\p{Punct}]{8,}$";;
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
