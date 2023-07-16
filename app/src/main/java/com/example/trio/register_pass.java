package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class register_pass extends AppCompatActivity {

    TextView register;
    private String TAG="REGISTER_PASS";
    EditText email,input1,input2,input3,input4;
    Button Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pass);
        register=findViewById(R.id.registerpass_title);
        email=findViewById(R.id.email_registerpass);
        input1=findViewById(R.id.input1_registerpass);
        input2=findViewById(R.id.input2_registerpass);
        input3=findViewById(R.id.input3_registerpass);
        input4=findViewById(R.id.input4_registerpass);
        Next=findViewById(R.id.next_registerpass);
        Intent i=getIntent();
        email.setText(i.getStringExtra("Email"));
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!input1.getText().toString().isEmpty()&&!input2.getText().toString().isEmpty()&&!input3.getText().toString().isEmpty()&&!input4.getText().toString().isEmpty()){
                    int a=Integer.parseInt(input1.getText().toString());
                    int b=Integer.parseInt(input2.getText().toString());
                    int c=Integer.parseInt(input3.getText().toString());
                    int d=Integer.parseInt(input4.getText().toString());
                    int password=(a*1000+b*100+c*10+d*1);
                    String email=i.getStringExtra("Email");
                    try {
                        startActivity(new Intent(register_pass.this,register_useredit.class));
                        postEmailPassword(email,password);
                    }
                    catch (JSONException e) {
                        Log.e(TAG,"Exception Occured "+e.getMessage());
                        throw new RuntimeException(e);
                    }

                }
            }
        });

    }

    private void postEmailPassword(String email, int password) throws JSONException {
        String url="http://10.11.6.27:3000/api/v1/users/signuppassword";
        RequestQueue queue= Volley.newRequestQueue(register_pass.this);
        JSONObject json=new JSONObject();
        json.put("email",email);
        json.put("password",password);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            try {
                                 String res=response.getString("status");
                                 if(res.equals("success")){
                                   startActivity(new Intent(register_pass.this,register_useredit.class));
                                 }
                                 else{
                                     String e=response.getString("message");
                                     Toast.makeText(register_pass.this, "Hello user "+e, Toast.LENGTH_SHORT).show();
                                 }
                            }
                            catch (Exception e){
                                Log.e(TAG,"Method Exception Occured "+e.getMessage());
                                Toast.makeText(register_pass.this, "Error due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(register_pass.this, "Error Occured "+error, Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);

    }
}