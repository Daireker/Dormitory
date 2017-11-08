package com.example.daireker.dormitory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBack, mMap , mShare, mUpdate;

    private TextView mName, mStuNum, mSex, mCondition, mBuildNum, mDorNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();

    }

    void initView(){
        mName = (TextView) findViewById(R.id.name);
        mStuNum = (TextView) findViewById(R.id.stuNum);
        mSex = (TextView) findViewById(R.id.sex);
        mCondition = (TextView) findViewById(R.id.tx_condition);
        mBuildNum = (TextView) findViewById(R.id.buildNum);
        mDorNum = (TextView) findViewById(R.id.dormitoryNum);

        mCondition.setOnClickListener(this);

        mBack = (ImageView) findViewById(R.id.btn_back);
        mBack.setOnClickListener(this);

        mMap = (ImageView) findViewById(R.id.schoolMap);
        mMap.setOnClickListener(this);

        mShare = (ImageView) findViewById(R.id.btn_share);
        mShare.setOnClickListener(this);

        mUpdate = (ImageView) findViewById(R.id.btn_update);
        mUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                Intent i = new Intent(this,Login.class);
                startActivity(i);
                finish();
                break;
            case R.id.schoolMap:
                Intent j = new Intent(this,Map.class);
                startActivity(j);
                finish();
                break;
            case R.id.tx_condition:
                Intent k = new Intent(this,ChooseDormitory.class);
                startActivity(k);
                finish();
                break;
            case R.id.btn_share:
                ToastUtil.showToast(MainActivity.this,"To Be Continue...");
                break;
            case R.id.btn_update:
                ToastUtil.showToast(MainActivity.this,"To Be Continue...");
                break;
            default:
                break;
        }

    }


}
