package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Year;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register_email extends AppCompatActivity {

    TextView register_title;
    EditText email_register;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        register_title=findViewById(R.id.register_title);
        email_register=findViewById(R.id.email_register);
        next=findViewById(R.id.next_register);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_register.getText().toString();
                if(!email.isEmpty()){
                    if(isValidEmail(email)){
                        int year=Integer.parseInt(email.substring(0,2));
                        int currentYear= Calendar.getInstance().get(Calendar.YEAR)%100;
                        if(Math.abs(currentYear-year)<5){
                           postEmail(email);
                        }
                        if(Math.abs(currentYear-year)>5){
                            email_register.setError("Sorry user you are not eligible");
                        }
                        else{
                            postEmail(email);
                        }
                    }
                    else{
                        email_register.setError("Please Enter the valid Email");
                    }

                }
                else{
                    email_register.setError("Email Field is Empty");
                }
            }
        });
    }

    private void postEmail(String email) {

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-z0-9+_.-]+@nec\\.edu\\.in$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}