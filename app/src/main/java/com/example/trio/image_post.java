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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trio.Storage.Storage;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class image_post extends AppCompatActivity {

    ImageButton back,post;
    ImageView view;
    ImageButton add;
    Storage store=new Storage();
    EditText captions,tag;
    CheckBox check;
    private ApiService apiService;
    String sImage;
    boolean mode=false;
    Spinner clubs;
    int selectedId;
    Uri uri;
    TextView change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);
        back=findViewById(R.id.backbtn);
        view=findViewById(R.id.image);
        clubs=findViewById(R.id.spinner_adt);
        add=findViewById(R.id.newPost);
        captions=findViewById(R.id.edit);
        tag=findViewById(R.id.edit_tag);
        check=findViewById(R.id.radio1);
        post=findViewById(R.id.tick);
        change=findViewById(R.id.change);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(image_post.this, android.R.layout.simple_spinner_item, store.getArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubs.setAdapter(adapter);
        clubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedId=store.club_name.get(position);
                Toast.makeText(image_post.this, "Hii"+selectedId, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(sImage.isEmpty()){
                            change.setError("NO Images Are Not yet selected");
                    }
                    else{
                        add.setVisibility(View.GONE);
                        ImagePicker.Companion.with(image_post.this)
                                .crop()
                                .maxResultSize(1380,1380)
                                .start(101);
                    }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption=captions.getText().toString();
                String tags=tag.getText().toString();
                Toast.makeText(image_post.this, "HII+POST", Toast.LENGTH_SHORT).show();
                if(!caption.isEmpty() && !tags.isEmpty() && selectedId!=0){
                    uploadImage(caption,tags,mode,"image",selectedId);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add.setVisibility(View.GONE);
                ImagePicker.Companion.with(image_post.this)
                        .crop()
                        .maxResultSize(1380,1380)
                        .start(101);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mode=true;
                }
                else{
                    mode=false;
                }
            }
        });
    }

    private void uploadImage(String caption, String tags, boolean mode,String format,int id) {
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
//        String token=store.getKeyUsername();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://10.11.6.27:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(store.getKeyUsername()))
                .build();
        File file=new File(sImage);
//        Log.d("file",file)
        if (!file.exists()) {
            Log.d("RISHI", "Image file does not exist");
            return;  // Exit the method if the image file is not found
        }
        else{
            Log.d("RISHI","IMAGE PRESENT");
        }
        Log.d("REQUEST",Uri.parse(sImage).getPath());
        RequestBody requestBody = RequestBody.create(file,MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        Log.d("RISHI","REQUEST BODY");
        apiService=retrofit.create(ApiService.class);
        Call<Example> call=apiService.uploadImage(body);
        Log.d("RISHI","ENQUEUE");
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("RISHI","RESPONSE RECEIVED");
                Log.d("RISHI", String.valueOf(response));
                Log.d("ANNAN", call.toString());

                if(response.isSuccessful()) {
                    getOtherData(caption,tags,mode,format,id);
                    Log.d("ANNAN", "HELLO");
                    Log.d("ANNAN",response.body().getMessage());
                    Log.d("ANNAN", response.body().toString());
                    Toast.makeText(image_post.this, "Post Created Successfully", Toast.LENGTH_SHORT).show();
                }

                else{
                    Log.d("RISHI", "Response not successful. Code: " + response.code());
                    Toast.makeText(image_post.this, "Post Not  Created Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t)  {
                Log.d("RESPONSE",t.toString());
                Toast.makeText(image_post.this, "Error Occured due to"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getOtherData(String caption, String tags, boolean mode, String format, int id) {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://10.11.6.27:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(store.getKeyUsername()))
                .build();
        File file=new File(Uri.parse(sImage).getPath());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("imgName",file.getName());
//        jsonObject.addProperty("imgName",file.getName());
        jsonObject.addProperty("caption", caption);
        jsonObject.addProperty("tags", tags);
        jsonObject.addProperty("modes", mode);
        jsonObject.addProperty("format", format);
        jsonObject.addProperty("club", id);
        Log.d("JSON", String.valueOf(jsonObject));
        String jsonBody = new Gson().toJson(jsonObject);
        Log.d("JSON", jsonBody);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonBody, mediaType);;
        apiService=retrofit.create(ApiService.class);
        Call<Example> call = apiService.getOtherData(requestBody);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("JSON", String.valueOf(response.code()));
                Log.d("RISHI","RESPONSE RECEIVED");
                Log.d("RISHI", String.valueOf(response));
                Log.d("BROTHER", call.toString());

                if(response.isSuccessful()) {
                    Log.d("BROTHER", "HELLO");
                    Log.d("BROTHER",response.body().getMessage());
                    Log.d("BROTHER", response.body().toString());
                    Toast.makeText(image_post.this, "Post Created Successfully", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(image_post.this, "Post Not  Created Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("RESPONSE",t.toString());
                Toast.makeText(image_post.this, "Error Occured due to"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OkHttpClient getOkHttpClient(final String authToken) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .method(original.method(), original.body());
                    Log.d("RISHI","REQUEST SEND");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .readTimeout(60, TimeUnit.SECONDS)   // Increase the read timeout
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK){
            uri=data.getData();
            Context context=image_post.this;
            sImage=RealPathUtil.getRealPath(context,uri);
            Bitmap bitmap= BitmapFactory.decodeFile(sImage);
            view.setImageBitmap(bitmap);
        }
    }
}