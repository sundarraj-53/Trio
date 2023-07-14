package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView login_title,account_login;
    EditText email_login,password_login;
    Button Login;
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
                Toast.makeText(MainActivity.this, "Hii"+account_login, Toast.LENGTH_SHORT).show();;
            }
        });
        account_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,register_email.class));
            }
        });

    }
}