package com.example.trio;

import androidx.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.trio.Storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class VolleyMultipartRequest extends Request<JSONObject> {
    private final Response.Listener<JSONObject> mListener;
    private final File mImageFile;
    private final String mStringParam1;
    private final String mStringParam2;
    private final boolean mBooleanParam;
    private final JSONObject mJsonObject;
    Storage store = new Storage();

    public VolleyMultipartRequest(String url, Response.Listener<JSONObject> mListener, Response.ErrorListener errorListener, File mImageFile, String mStringParam1, String mStringParam2, boolean mBooleanParam, JSONObject mJsonObject) {
        super(Method.POST, url, errorListener);
        this.mListener = mListener;
        this.mImageFile = mImageFile;
        this.mStringParam1 = mStringParam1;
        this.mStringParam2 = mStringParam2;
        this.mBooleanParam = mBooleanParam;
        this.mJsonObject = mJsonObject;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("captions", mStringParam1);
        params.put("tags", mStringParam2);
        params.put("modes", String.valueOf(mBooleanParam));
        return params;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + store.getKeyUsername());
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
       try{
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("captions", mStringParam1)
                .addFormDataPart("tags", mStringParam2)
                .addFormDataPart("modes", String.valueOf(mBooleanParam))
                .addFormDataPart("image", "image.jpg", RequestBody.create(MediaType.parse("image/jpeg"), mImageFile));
           builder.addPart(RequestBody.create(MediaType.parse("application/json"), mJsonObject.toString()));


           okhttp3.RequestBody requestBody = builder.build();
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(bos));
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
            bufferedSink.close();
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }

        return bos.toByteArray();
    }


    private RequestBody createRequestBody(final File imageFile) {
        return new RequestBody() {
            @Override
            public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
                try (okio.Source source = Okio.source(imageFile)) {
                    bufferedSink.writeAll(source);
                }
            }

            @Override
            public MediaType contentType() {
                return MediaType.parse("image/jpeg");
            }
        };
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new VolleyError("Unable to parse JSON response."));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
