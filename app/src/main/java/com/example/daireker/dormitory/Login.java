package com.example.daireker.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements View.OnClickListener{

    public static boolean canSee = false;

    private EditText mID, mPassword;

    private Button mLogin, mCondition;

    private TextView mForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
    }

    void initView(){
        mID = (EditText) findViewById(R.id.inputID);

        mPassword = (EditText) findViewById(R.id.inputPassword);
        mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mCondition = (Button) findViewById(R.id.condition);
        mCondition.setOnClickListener(this);

        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(this);

        mForget = (TextView) findViewById(R.id.forget);
        mForget.setText(Html.fromHtml("<a href='https://iaaa.pku.edu.cn/iaaa/pageFlows/authen/findPwd2.jsp'>忘记密码</a>"));
        mForget.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                queryLogin(mID.getText().toString(),mPassword.getText().toString());
                //check();
                break;
            case R.id.condition:
                if(canSee == false){
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    canSee = true;
                }else {
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    canSee = false;
                }
                break;
            default:
                break;
        }
    }

    public void check(){
        if(mID.getText().toString().length()==0 ||
                mPassword.getText().toString().length()==0){
            ToastUtil.showToast(Login.this,"请输入账户或密码");
            //Toast.makeText(Login.this,"请输入账户或密码",Toast.LENGTH_SHORT).show();
        }else if (mID.getText().toString().equals("000000") &&
                mPassword.getText().toString().equals("000000")){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }else{
            ToastUtil.showToast(Login.this,"账户或密码不正确");
            //Toast.makeText(Login.this,"账户或密码不正确",Toast.LENGTH_SHORT).show();
        }
    }

    public void queryLogin(String username,String password){
        if(username.length()==0 || password.length()==0){
            ToastUtil.showToast(Login.this,"请输入账户或密码");
            return;
        }
        String data = "username=" + username + "&password=" + password;
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?" + data;
        Log.d("Login",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    HTTPSTrustManager.allowAllSSL();
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);

                    int responseCode = con.getResponseCode();
                    if(responseCode == 200){
                        InputStream inputStream = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer buffer = new StringBuffer();
                        String str;
                        while ((str=reader.readLine()) != null){
                            buffer.append(str + "\n");
                        }
                        Log.d("Login",buffer.toString());
                    }else{
                        Log.d("Login","访问失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

}
