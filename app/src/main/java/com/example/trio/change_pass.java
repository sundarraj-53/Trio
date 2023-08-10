package com.example.trio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

public class change_pass extends AppCompatActivity {

    EditText oldpass,newpass,confirmpass;
    AppCompatButton submit;
    TextView back_btn;
    Storage store=new Storage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        oldpass=findViewById(R.id.passEt);
        newpass=findViewById(R.id.newEt);
        confirmpass=findViewById(R.id.confirmEt);
        submit=findViewById(R.id.media);
        back_btn=findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=oldpass.getText().toString();
                String newPassword=newpass.getText().toString();
                String confirmPassword=confirmpass.getText().toString();
                if(!password.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                        if (newPassword.equals(confirmPassword)) {
                            try {
                                sendPassword(password, newPassword);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        } else {
                            confirmpass.setError("Confirm password does not matches");
                        }
                }
                else{
                    if(password.isEmpty()){
                        oldpass.setError("Enter your old password");
                    }
                    if(newPassword.isEmpty()){
                        newpass.setError("Enter your new password");
                    }
                    if(confirmPassword.isEmpty()){
                        confirmpass.setError("Enter your confirm password");
                    }
                }
            }
        });
    }

    private void sendPassword(String password, String newPassword) throws JSONException {
//        String url="http://10.11.6.27:3000/api/v1/auth/changepassword";
        String url="https://ecapp.onrender.com/api/v1/auth/changepassword";
        RequestQueue queue= Volley.newRequestQueue(change_pass.this);
        JSONObject json=new JSONObject();
        json.put("oldPassword",password);
        json.put("newPassword",newPassword);
        Log.d("PASSWORD",password+" "+newPassword);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.PATCH, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("PASSWORD",response.toString());
                        try {
                            String res = response.getString("status");

                            if(res.equals("success")){
                                oldpass.setText(" ");
                                newpass.setText(" ");
                                confirmpass.setText(" ");
                                Toast.makeText(change_pass.this, "Your password updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(change_pass.this, "Sorry User...!"+response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("PASSWORD",e.getMessage());
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("PASSWORD",error.getMessage());
                        error.printStackTrace();
                        Log.d("register_pass",error.toString());
                        Toast.makeText(change_pass.this, "Failed to connect to server..!", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                Log.d("PASSWORD",token);
                headers.put("Authorization","Bearer " + token);
                return headers;
            }

        };
        queue.add(request);


    }
}