package com.example.trio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class commitee_activity extends AppCompatActivity {
    Spinner clubs;
    ArrayList<commitee> clubSpinner;
   Storage storage=new Storage();
    private ProgressBar PB;
    TextView back_id;
    EditText email;
    Button submit;
    Storage store=new Storage();
    int selectedId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitee);
        clubs=findViewById(R.id.spinner_adt);
        email=findViewById(R.id.email_et);
        PB=findViewById(R.id.idPBLoading);
        clubSpinner=new ArrayList<>();
        back_id=findViewById(R.id.back);
        submit=findViewById(R.id.submit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(commitee_activity.this, android.R.layout.simple_spinner_item, store.getArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubs.setAdapter(adapter);
        back_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        clubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   selectedId=store.club_name.get(position);


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=email.getText().toString();
                String clubName=clubs.getSelectedItem().toString();
                if(!Email.isEmpty() && !clubName.isEmpty()){
                    if(isValidEmail(Email)){
                        try {
                            PB.setVisibility(View.VISIBLE);
                            sendData(Email,selectedId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        email.setError("Email is Not valid");
                    }
                }
                else{
                    if(Email.isEmpty()){
                        email.setError("Email cannot be Empty");
                    }
                }

            }
        });
    }
    private void sendData(String email, int selectedId) throws JSONException {
//        String url="http://10.11.6.27:3000/api/v1/clubs/club";
        String url ="https://ecapp.onrender.com/api/v1/clubs/club";
        RequestQueue queue= Volley.newRequestQueue(commitee_activity.this);
        JSONObject json=new JSONObject();
        json.put("email",email);
        json.put("clubId",selectedId);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try {
                            if(response.getString("status").equals("success")){
                                Toast.makeText(commitee_activity.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(commitee_activity.this,commitee_activity.class));
                            }
                            else{
                                Toast.makeText(commitee_activity.this, "Failed to Add the Student", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                        Toast.makeText(commitee_activity.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-z0-9+_.-]+@nec\\.edu\\.in$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}