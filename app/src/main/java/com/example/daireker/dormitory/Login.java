package com.example.daireker.dormitory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                check();
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
}
