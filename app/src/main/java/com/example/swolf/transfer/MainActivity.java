package com.example.swolf.transfer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaTimestamp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.OkHeaders;

/**
 * Created by swolf on 16/11/4.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Here....");
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.send);
        final EditText contentText = (EditText) findViewById(R.id.content);
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = contentText.getText().toString().toUpperCase();
                final RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, content);
                final String url = "http://192.168.1.102:50000/";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient client = new HttpClient();
                        System.out.println("Send>>>>>>>>>>>>");
                        client.post(url, body);
                        System.out.println("Send over========>>>>>>");
                    }
                }).start();
            }
        });


        Button sendFileButton = (Button) findViewById(R.id.sendFileButton);
        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendFileActivity.class);
                startActivity(intent);
            }
        });
    }
}
