package com.example.trio;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class register_useredit extends AppCompatActivity {

    private static final String TAG="REGISTER_USEREDIT";
    static String name="";
    TextView title;
    RadioGroup radio;
    RadioButton button1,button2;
    ViewStub view1,view2;
    EditText studfname,studlname,studdepartment,studpassword,studconfirmpassword,studphoneno,studposition;
    EditText facufname,faculname,facudepartment,facupassword,facuconfirmpassword,facuphoneno,facuposition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_useredit);
        title=findViewById(R.id.registeruser_title);
        radio=findViewById(R.id.radioGroup_registeruser);
        view1=findViewById(R.id.studentviewStub);
        view2=findViewById(R.id.facultyviewStub);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 radio.setVisibility(View.INVISIBLE);
                if(checkedId==R.id.student_registeruser){
                    view1.setVisibility(View.VISIBLE);
                    name="Student";
                    Toast.makeText(register_useredit.this, "Clicked Student", Toast.LENGTH_SHORT).show();
                }
               else{
                   view1.setVisibility(View.GONE);
                    name="Faculty";
                   view2.setVisibility(View.VISIBLE);
                }
            }
        });
        if(name.equals("Student")){

        }
        if(name.equals("Faculty")){

        }
    }
}