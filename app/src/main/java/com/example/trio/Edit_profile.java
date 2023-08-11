package com.example.trio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trio.Storage.Storage;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Edit_profile extends AppCompatActivity {

    EditText first, last, phoneno;
    Spinner department_r;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    public String Phoneno, Department;
    private ApiService apiService;
    Button submit;
    Storage store = new Storage();
    ImageButton newadd;
    TextView back;
    public int select=-1;
    private ProgressBar PB;
    Uri uri;
    String sImage;
    ArrayAdapter<String> adapter;
    String[] Dept = {"", "IT", "ECE", "EEE", "MECH", "CSE", "CIVIL"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        first = findViewById(R.id.firstname);
        PB=findViewById(R.id.idPBLoading);
        last = findViewById(R.id.lastname);
        phoneno = findViewById(R.id.phoneNoEt);
        department_r = findViewById(R.id.DepartmentEt);
        submit = findViewById(R.id.editProfile);
        newadd=findViewById(R.id.addicon);
        back=findViewById(R.id.back);
        circleImageView=findViewById(R.id.user_profile);
        adapter=setAdapter();
        PB.setVisibility(View.VISIBLE);
        loadUserDetails();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserDetails();
            }
        });
        newadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Edit_profile.this)
                        .crop()
                        .maxResultSize(1380,1380)
                        .start(101);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK){
            uri=data.getData();
            Context context=Edit_profile.this;
            sImage=RealPathUtil.getRealPath(context,uri);
            Bitmap bitmap= BitmapFactory.decodeFile(sImage);
            circleImageView.setImageBitmap(bitmap);
        }
    }

    private void sendUserDetails()
    {

        if(sImage.isEmpty()) {
            String firstName = first.getText().toString();
            String lastName = last.getText().toString();
            String phoneNo = phoneno.getText().toString();
            if(!department_r.getSelectedItem().toString().equals(Department)){
                Department=department_r.getSelectedItem().toString();
            }
            if (!firstName.isEmpty() && !lastName.isEmpty() && !Department.isEmpty() && !phoneNo.isEmpty()) {
                try {
                    PB.setVisibility(View.VISIBLE);
                    postMethod(firstName, lastName, Department, phoneNo);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (firstName.isEmpty()) {
                    first.setError("First name should not be empty");
                }
                if (lastName.isEmpty()) {
                    last.setError("Last Name Should not be empty");
                }
                if (phoneNo.isEmpty()) {
                    phoneno.setError("Phone No should Not be entered");
                }
            }
        }
        else{
            String firstName = first.getText().toString();
            String lastName = last.getText().toString();
            String phoneNo = phoneno.getText().toString();
            if(!department_r.getSelectedItem().toString().equals(Department)){
                Department=department_r.getSelectedItem().toString();
            }
            if (!firstName.isEmpty() && !lastName.isEmpty() && !Department.isEmpty() && !phoneNo.isEmpty()) {
                try {
                    userImage();
                    PB.setVisibility(View.VISIBLE);
                    postMethod(firstName, lastName, Department, phoneNo);
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private void userImage() {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://10.11.6.27:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(store.getKeyUsername()))
                .build();
          File file=new File(sImage);
        if (!file.exists()) {
            Log.d("RISHI", "Image file does not exist");
            return;  // Exit the method if the image file is not found
        }
        else{
            Log.d("RISHI","IMAGE PRESENT");
        }
        Log.d("REQUEST",Uri.parse(sImage).getPath());
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        Log.d("RISHI","REQUEST BODY");
        apiService=retrofit.create(ApiService.class);
        Call<Example> call=apiService.userImage(body);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                Log.d("RISHI","RESPONSE RECEIVED");
                Log.d("RISHI", String.valueOf(response));
                Log.d("ANNAN", call.toString());
                if(response.isSuccessful()) {
                    Log.d("ANNAN", "HELLO");
                    Log.d("ANNAN",response.body().getMessage());
                    Log.d("ANNAN", response.body().toString());
                    try {
                        sendFileName();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                else{
                    Log.d("RISHI", "Response not successful. Code: " + response.code());
                    Toast.makeText(Edit_profile.this, "Post Not  Created Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t)  {
                PB.setVisibility(View.GONE);
                Log.d("RESPONSE",t.toString());
                Toast.makeText(Edit_profile.this, "Error Occured due to"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendFileName() throws JSONException {
        String url="http://10.11.6.27:3000/api/v1/users/user/profileimagename";
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        File file=new File(sImage);
        JSONObject userDetails =new JSONObject();
        userDetails.put("originalname",file.getName());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.PATCH, url,userDetails,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try{
                            Log.d("JSONOBJECT","HII");
                            String res=response.getString("status");
                            if(res.equals("success")){
                                Toast.makeText(Edit_profile.this, "Your profile is updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Toast.makeText(Edit_profile.this, "Method Exception Occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Edit_profile.this, "Error Occured "+error, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                PB.setVisibility(View.GONE);
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }

    private OkHttpClient getOkHttpClient(final String authToken) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .method(original.method(), original.body());
                    Log.d("RISHI","REQUEST SEND");
                    okhttp3.Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .readTimeout(60, TimeUnit.SECONDS)   // Increase the read timeout
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private void postMethod(String firstName,String lastName,String department,String phoneNo) throws JSONException
    {
//        String url="http://10.11.6.27:3000/api/v1/users/user/updateDetail";
        String url="https://ecapp.onrender.com/api/v1/users/user/updateDetail";
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        JSONObject userDetails =new JSONObject();
        userDetails.put("firstName",firstName);
        userDetails.put("lastName",lastName);
        userDetails.put("department",department);
        userDetails.put("phoneNo",phoneNo);
        JSONObject finaljson=new JSONObject();
        finaljson.put("userDetail",userDetails);
        Log.d("NAGRAJAN", String.valueOf(userDetails));
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.PATCH, url,finaljson,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try{
                            Log.d("JSONOBJECT","HII");
                            String res=response.getString("status");
                            if(res.equals("success")){
                               onBackPressed();
                            }
                        }
                        catch (JSONException e){
                            Toast.makeText(Edit_profile.this, "Method Exception Occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                        error.printStackTrace();
                        Toast.makeText(Edit_profile.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }
    public ArrayAdapter<String> setAdapter()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Edit_profile.this, android.R.layout.simple_spinner_dropdown_item, Dept);
        department_r.setAdapter(adapter);
        return adapter;
    }
    private void loadUserDetails()
    {
//        String url="http://10.11.6.27:3000/api/v1/users/user";
        String url="https://ecapp.onrender.com/api/v1/users/user";
        JSONObject json=new JSONObject();
        RequestQueue queue= Volley.newRequestQueue(Edit_profile.this);
        ArrayList subt=new ArrayList();
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PB.setVisibility(View.GONE);
                        try {
                            response=response.getJSONObject("data").getJSONObject("user");
                            Log.d("ASHWIN", String.valueOf(response));
                            String firstN =response.getString("firstName");
                            String lastN = response.getString("lastName");
                            String photo=response.getString("profileLink");
                            photo= photo.replace("\\/", "/");
                            Log.d("ASHWIN", photo);
                            if(!photo.isEmpty()) {
                                Picasso.get()
                                        .load(photo)
                                        .into(circleImageView);
                            }
                            Department = response.getString("department");
                            Phoneno = response.getString("phoneNo");
                            select=adapter.getPosition(Department);
                            first.setText(firstN);
                            last.setText(lastN);
                            phoneno.setText(Phoneno);
                            if(select>0){
                                department_r.setSelection(select);
                            }
                        }
                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PB.setVisibility(View.GONE);
                            Toast.makeText(Edit_profile.this, "Failed to connect server..!", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token=store.getKeyUsername();
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }
}