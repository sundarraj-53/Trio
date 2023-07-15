package com.example.trio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class register_user extends AppCompatActivity {

    TextView title;
    RadioGroup radio;
    RadioButton button1,button2;
    RelativeLayout container1,container2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        title=findViewById(R.id.registeruser_title);
        radio=findViewById(R.id.registeruser_title);
        button1=findViewById(R.id.student_registeruser);
        button2=findViewById(R.id.faculty_registeruser);
        container1=findViewById(R.id.Container1_registeruser);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               container1.setVisibility(View.GONE);
                if(checkedId==R.id.student_registeruser){
                        container1.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}