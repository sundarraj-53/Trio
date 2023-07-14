package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signupemail extends AppCompatActivity {

    TextView register_title;
    EditText email_register;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupemail);
        register_title=findViewById(R.id.register_title);
        email_register=findViewById(R.id.email_register);
        next=findViewById(R.id.next_register);
        Toast.makeText(this, ""+next, Toast.LENGTH_SHORT).show();
    }
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-z0-9+_.-]+@nec\\.edu\\.in$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}