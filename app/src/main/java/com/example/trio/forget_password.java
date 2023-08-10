package com.example.trio;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forget_password extends AppCompatActivity {

    EditText mail;
    private ProgressBar PB;
    ImageButton backbtn;
    AppCompatButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mail=findViewById(R.id.emailEt);
        submit=findViewById(R.id.media);
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mail.getText().toString();
                if(!email.isEmpty()){
                    if(isValidEmail(email)){
                        try {
                            sendEmail(email);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        mail.setError("Provide the college Email domain");
                    }
                }
                else{
                    mail.setError("Email can not be empty");
                }
            }
        });

    }

    private void sendEmail(String email) throws JSONException {
//        String url="http://10.11.6.27:3000/api/v1/auth/forgotpassword";
        String url="https://ecapp.onrender.com/api/v1/auth/forgotpassword";
        JSONObject json=new JSONObject();
        json.put("email",email);
        RequestQueue queue= Volley.newRequestQueue(forget_password.this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("success")){
                                mail.setText(" ");
                                Toast.makeText(forget_password.this, "Change password link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(forget_password.this, "Error occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e){
                            Toast.makeText(forget_password.this, "Sorry user...!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                        Toast.makeText(forget_password.this, "Failed to connect the server..!", Toast.LENGTH_SHORT).show();
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