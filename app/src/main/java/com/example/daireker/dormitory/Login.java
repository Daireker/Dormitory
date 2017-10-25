package com.example.daireker.dormitory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText mID, mPassword;

    private Button mLogin;

    private TextView mForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(this);

        mForget = (TextView) findViewById(R.id.forget);
        mForget.setText(Html.fromHtml("<a href='http://www.baidu.com'>忘记密码</a>"));
        mForget.setMovementMethod(LinkMovementMethod.getInstance());
        //mForget.setOnClickListener(this);
    }

    void initView(){
        mID = (EditText) findViewById(R.id.inputID);
        mPassword = (EditText) findViewById(R.id.inputPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;
            //case R.id.forget:
            //  break;
            default:
                break;
        }
    }
}
