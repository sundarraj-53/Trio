package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;
import com.example.trio.signUp.register;
import com.example.trio.signUp.register_email;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView login_title,account_login;
    EditText email_login,password_login;
    Button Login;
    Storage store=new Storage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_title=findViewById(R.id.title_login);
        account_login=findViewById(R.id.createaccount_login);
        email_login=findViewById(R.id.email_login);
        password_login=findViewById(R.id.password_login);
        Login=findViewById(R.id.login_button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_login.getText().toString();
                String password=password_login.getText().toString();
              //Working Purpose
                startActivity(new Intent(MainActivity.this, home.class));

               //end
                if(!email.isEmpty() && !password.isEmpty()){
                    if(isValidEmail(email)){
                        int year=Integer.parseInt(email.substring(0,2));
                        int currentYear= Calendar.getInstance().get(Calendar.YEAR)%100;
                        if(Math.abs(currentYear-year)>5){
                            email_login.setError("Sorry user you are not eligible");
                        }
                        if(Math.abs(currentYear-year)<5){
                            try {
                                postEmail(email,password);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else{
                            try {
                                postEmail(email,password);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    if(!isValidEmail(email)){
                        email_login.setError("Please Enter our college Email");
                    }
                }
                if(email.isEmpty()){
                    email_login.setError("Email is Empty");
                }
                if(password.isEmpty()){
                    password_login.setError("Password is Empty");
                }
            }
        });
        account_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register_email.class));
            }
        });

    }

    private void postEmail(String email,String password) throws JSONException{
        String url="http://10.11.6.27:3000/api/v1/users/login";
        JSONObject json=new JSONObject();
        json.put("email",email);
        json.put("password",password);
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res=response.getString("status");
                            String token=response.getString("token");
                            if(res.equals("success")){
                                store.saveUsername(token);
//                                startActivity(new Intent(MainActivity.this,home.class));
                            }
                            if(res.equals("error")){
                                password_login.setError(res);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 300) {
                            startActivity(new Intent(MainActivity.this, register.class));

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        queue.add(request);
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-z0-9+_.-]+@nec\\.edu\\.in$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}