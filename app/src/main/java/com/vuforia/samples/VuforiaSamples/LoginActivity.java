package com.vuforia.samples.VuforiaSamples;

import android.app.ProgressDialog;
import android.content.Entity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hereiam.app.MyApp;
import com.vuforia.samples.entity.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private String url = "http://10.0.2.2:8080/AndroidNetwork/LoginServlet";
    private Handler handler;
    private String responseText;
    private ProgressDialog loginDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("正在验证登录信息...");
        handler = new Handler(){
            public void handleMessage(Message msg){

                super.handleMessage(msg);

                loginDialog.dismiss();

                if (msg.what==1){
                    Gson gson = new Gson();
                    User user = gson.fromJson(responseText,User.class);

                    if (user!=null){
                        MyApp myApp = (MyApp)getApplication();
                        myApp.setUser(user);
                        Intent intent = new Intent(LoginActivity.this, ShowMapActivity.class);
                        startActivity(intent);

                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"网络连接失败！",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }
    public void config(View v){

        loginDialog.show();
        new Thread(new LoginRunner()).start();
    }

    private class LoginRunner implements Runnable{
        public  void run(){
            HttpClient client = new DefaultHttpClient();

            HttpPost request = new HttpPost(url);

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username",edtUsername.getText().toString()));
                params.add(new BasicNameValuePair("password",edtPassword.getText().toString()));

                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse response = client.execute(request);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    responseText = EntityUtils.toString(response.getEntity());

                    handler.sendEmptyMessage(1);
                }
            }catch (ClientProtocolException e){
                e.printStackTrace();
                handler.sendEmptyMessage(-1);
            }catch (IOException e){
                e.printStackTrace();
                handler.sendEmptyMessage(-1);
            }
        }
    }


    public void rigister(View v){
        Intent intent = new Intent(LoginActivity.this,
               RigisterActivity.class);
        startActivity(intent);
    }
}
