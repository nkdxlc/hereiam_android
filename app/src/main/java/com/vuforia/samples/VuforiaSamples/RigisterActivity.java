package com.vuforia.samples.VuforiaSamples;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.vuforia.samples.entity.User;

import org.apache.http.client.HttpClient;

public class RigisterActivity extends AppCompatActivity {

    private EditText edtRname;
    private EditText edtRpassword;
    private String url = "http://10.0.2.2:8080/AndroidNetwork/RegisterServlet";
    private Handler handler;
    private String responseText;
    private ProgressDialog registerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);

        edtRname = (EditText) findViewById(R.id.edtRname);
        edtRpassword = (EditText)findViewById(R.id.edtRpassword);
        registerDialog = new ProgressDialog(this);
        registerDialog.setMessage("正在记录注册信息...");

    }

    public void finish(View v){
        Intent intent = new Intent(RigisterActivity.this,
                ShowMapActivity.class);
        startActivity(intent);
    }
}
