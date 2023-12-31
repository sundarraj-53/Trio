package com.example.trio.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register_email extends AppCompatActivity{

    TextView register_title;
    private ProgressBar PB;
    private static final int MY_TIMEOUT_MS = 1000000;
    EditText email_register;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        register_title=findViewById(R.id.register_title);
        PB=findViewById(R.id.idPBLoading);
        email_register=findViewById(R.id.email_register);
        next=findViewById(R.id.next_register);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_register.getText().toString();
                if(!email.isEmpty()){
                    if(isValidEmail(email)) {
                        if(Character.isDigit(email.charAt(0))) {
                            int year = Integer.parseInt(email.substring(0, 2));
                            int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
                            PB.setVisibility(View.VISIBLE);
                            if (Math.abs(currentYear - year) > 4) {
                                email_register.setError("Sorry user you are not eligible");
                            }
                            if (Math.abs(currentYear - year) < 5) {
                                try {
                                    String Student="Student";
                                    postEmail(email,Student);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        else {
                                try {
                                    String Student="Faculty";
                                    postEmail(email,Student);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    else{
                        PB.setVisibility(View.GONE);
                        email_register.setError("Please Enter our college Email");
                    }

                }
                else{
                    email_register.setError("Email Field is Empty");
                }
            }
        });
    }

    private void postEmail(String email,String Student) throws JSONException {
//        String url="http://10.11.6.27:3000/api/v1/auth/signupemail";
        String url="https://ecapp.onrender.com/api/v1/auth/signupemail";
        JSONObject json=new JSONObject();
        json.put("email",email);
        RequestQueue queue= Volley.newRequestQueue(register_email.this);

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                          PB.setVisibility(View.GONE);
                        try {
                            String res = response.getString("status");
                            if (res.equals("success")) {
                               Intent i=new Intent(register_email.this, register_pass.class);
                               i.putExtra("Email",email);
                               i.putExtra("Type",Student);
                               startActivity(i);
                            }
                            else
                            {
                                String e=response.getString("message");
                                Toast.makeText(register_email.this, "You already used the email", Toast.LENGTH_SHORT).show();
                                email_register.setError(e);
                            }
                        } catch (Exception e) {
                            Log.e("Hello", "JSONException: " + e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(register_email.this, "Registeration Failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                        Log.d("Hii", String.valueOf(error));
                        Toast.makeText(register_email.this, "Failed to connect to server..!", Toast.LENGTH_LONG).show();

                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(request);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-z0-9+_.-]+@nec\\.edu\\.in$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}