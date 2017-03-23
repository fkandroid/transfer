package com.example.swolf.transfer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by swolf on 16/11/5.
 */
public class SendFileActivity extends AppCompatActivity{

    File[] files;
    File parentDirectory;
    File currentFile;
    TextView textView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        textView = (TextView) findViewById(R.id.currentFile);
        listView = (ListView) findViewById(R.id.fileList);
        Button returnButton = (Button) findViewById(R.id.returnButton);
        Button sendFileButton = (Button) findViewById(R.id.sendFileButton);

        File sdcardDir = Environment.getExternalStorageDirectory();
        currentFile = sdcardDir;
        parentDirectory = sdcardDir;
        files = sdcardDir.listFiles();
        System.out.println("File lenth: " + files.length);

        inflateListView(files);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFile = files[position];
                textView.setText("当前选择的文件为: " + currentFile.getName());
                if (files[position].isFile()) {
                    return;
                }

                File[] tmp = files[position].listFiles();

                if (tmp.length == 0) {
                    Toast.makeText(SendFileActivity.this, "此文件夹下无文件或不可读",
                            Toast.LENGTH_LONG).show();
                } else {
                    parentDirectory = files[position];
                    files = tmp;
                    inflateListView(files);
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentDirectory = parentDirectory.getParentFile();
                currentFile = parentDirectory;
                textView.setText("当前选择的文件为: " + currentFile.getName());
                files = parentDirectory.listFiles();
                inflateListView(files);
            }
        });

        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
                final RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, currentFile);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient client = new HttpClient();
                        client.post("http://192.168.1.102:50000/file", body);
                    }
                }).start();
            }
        });

    }

    private void inflateListView(File[] files){
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

        System.out.println("File =========>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(files.length);
        for (int i = 0; i < files.length; i++){
            Map<String, Object> fileInfo = new HashMap<String, Object>();
            if(files[i].isDirectory()){
                fileInfo.put("icon", R.mipmap.folder);
            } else {
                fileInfo.put("icon", R.mipmap.file);
            }
            fileInfo.put("name", files[i].getName());

            listItems.add(fileInfo);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                listItems,
                R.layout.file_item,
                new String[] {"icon", "name"},
                new int[]{R.id.icon, R.id.name}
        );
        System.out.println(adapter.toString());
        System.out.println(listView.toString());

        listView.setAdapter(adapter);
    }
}
