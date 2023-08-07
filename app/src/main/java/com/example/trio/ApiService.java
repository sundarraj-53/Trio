package com.example.trio;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("posts/")
    Call<Example> uploadImage(
            @Part MultipartBody.Part image
    );
    @POST("posts/postDetail")
    Call<Example> getOtherData(
            @Body  RequestBody requestBody
    );
}
