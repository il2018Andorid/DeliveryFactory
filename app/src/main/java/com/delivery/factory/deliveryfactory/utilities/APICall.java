package com.delivery.factory.deliveryfactory.utilities;

import android.os.StrictMode;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jay-Andriod on 05-Apr-17.
 */

public class APICall {


    public static final MediaType JSON = MediaType.parse("application/json");

    static OkHttpClient client = new OkHttpClient();

    public static String post(String url, RequestBody body, String method) throws IOException {

        client.connectTimeoutMillis();
        client.readTimeoutMillis();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (method.equalsIgnoreCase("post")) {
            Log.d("data send--",""+body.toString());
            Request request = new Request.Builder()
                    .url(url)
                   // .addHeader("Content-Type","application/json")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } else if (method.equalsIgnoreCase("get")) {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } else if (method.equalsIgnoreCase("put")) {
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }else if(method.equalsIgnoreCase("delete")){
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
        return null;
    }



    public static String POST_MULTIPART(String url,String filename,File file,String service_provider_id) throws IOException {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        MediaType MEDIA_TYPE = MediaType.parse("image/*" ); // e.g. "image/png"

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("service_provider_id",service_provider_id)
                .addFormDataPart("cake_image_url","test")
                /*.addFormDataPart("filename",filename)*/ //e.g. title.png --> imageFormat = png
                .addFormDataPart("file", filename, RequestBody.create(MEDIA_TYPE, file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
