package com.example.trio;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class register_useredit extends AppCompatActivity {

    private static final String TAG="REGISTER_USEREDIT";
    static String name="";
    TextView title;
    RadioGroup radio;
    RadioButton button1,button2;
    ViewStub view1;
    EditText fname,lname,Password,confirmpassword,phoneno;
    Spinner department,studposition;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_useredit);
        title=findViewById(R.id.registeruser_title);
        radio=findViewById(R.id.radioGroup_registeruser);
        view1=findViewById(R.id.studentviewStub);
        fname=findViewById(R.id.Fname_registeruser_student);
        lname=findViewById(R.id.Lname_registeruser_student);
        department=findViewById(R.id.Department_registeruser_student);
        Password=findViewById(R.id.password_registeruser_student);
        confirmpassword=findViewById(R.id.conpassword_registeruser_student);
        phoneno=findViewById(R.id.conpassword_registeruser_student);
        studposition=findViewById(R.id.position_registeruser_student);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 radio.setVisibility(View.INVISIBLE);
                if(checkedId==R.id.student_registeruser){
                    view1.setVisibility(View.VISIBLE);
                    name="Student";
                   getStudent(name);
                }
               else{
                    name="Faculty";
                   view1.setVisibility(View.VISIBLE);
                    getFaculty(name);
                }
            }
        });

    }

    private void getFaculty(String name) {
        String firstName=fname.getText().toString();
        String lastName=lname.getText().toString();
        String department=setAdapter();
        String password=Password.getText().toString();
        String confirmPassword=confirmpassword.getText().toString();
        String phoneNo=phoneno.getText().toString();
        String studPosition=getAdapter();

    }
    private String getAdapter(){
        final String[] item = new String[1];
        studposition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item[0] =parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add(" ");
        arrayList.add("Member");
        arrayList.add("President");
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);
        return item[0];
    }

    private String setAdapter(){
        final String[] item = new String[1];
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item[0] =parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("IT");
        arrayList.add("CSE");
        arrayList.add("ECE");
        arrayList.add("EEE");
        arrayList.add("MECH");
        arrayList.add("CIVIL");
        arrayList.add("AI&DS");
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        department.setAdapter(adapter);
        return item[0];
    }
    private void getStudent(String name) {
    }
}