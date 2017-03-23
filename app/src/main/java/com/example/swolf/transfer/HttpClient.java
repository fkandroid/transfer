package com.example.swolf.transfer;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by swolf on 16/11/5.
 */
public class HttpClient {

    private OkHttpClient client;

    public HttpClient() {
        client = new OkHttpClient();
    }

    public Response post(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            return client.newCall(request).execute();
        } catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }
    
    public Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }
}
