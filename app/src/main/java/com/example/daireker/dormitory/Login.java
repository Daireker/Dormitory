package com.example.daireker.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements View.OnClickListener{

    public static boolean canSee = false;

    private static final int LOGIN_SUCCESS_MESSAGE = 1;

    private static final int LOGIN_ERROR_MESSAGE = 2;

    private EditText mID, mPassword;

    private Button mLogin, mCondition;

    private TextView mForget;

    private String errMessage, studentID;

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
                if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                    Log.d("Login","网络OK");
                    ToastUtil.showToast(Login.this,"网络OK！");
                    queryLogin(mID.getText().toString(),mPassword.getText().toString());
                }else{
                    Log.d("Login","网络挂了");
                    ToastUtil.showToast(Login.this,"网络挂了！");
                }
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

    private void queryLogin(String username,String password){
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
                        String bufferStr = buffer.toString();
                        Log.d("Login",bufferStr);
                        getLoginJson(bufferStr);
                    }else{
                        Log.d("Login","连接失败");
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

    private void getLoginJson(String JsonData){
        try {
            Log.d("Login","JsonData = " + JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            int errcode = jsonObject.getInt("errcode");
            Log.d("Login","errcode = " + errcode);
            JSONObject errmsgObject = jsonObject.getJSONObject("data");
            String errmsg = errmsgObject.getString("errmsg");
            Log.d("Login","errmsg = " + errmsg);

            errMessage = errmsg;
            Message msg = new Message();
            if(errcode == 0){
                studentID = mID.getText().toString();
                msg.what = LOGIN_SUCCESS_MESSAGE;
            }else{
                msg.what = LOGIN_ERROR_MESSAGE;
            }
            mHandler.sendMessage(msg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            Log.d("Login","handler启动");
            switch (msg.what){
                case LOGIN_ERROR_MESSAGE:
                    ToastUtil.showToast(Login.this,errMessage);
                    break;
                case LOGIN_SUCCESS_MESSAGE:
                    Intent i = new Intent(Login.this,MainActivity.class);
                    i.putExtra("stuid",studentID);
                    startActivity(i);
                    finish();
                default:
                    break;
            }
        }
    };

}
