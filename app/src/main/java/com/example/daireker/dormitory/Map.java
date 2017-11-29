package com.example.daireker.dormitory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Map extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackMain;

    private String stuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        initView();
        Intent i = getIntent();
        stuid = i.getStringExtra("stuid");
        Log.d("Map","stuid = " + stuid);
    }

    void initView(){
        mBackMain = (ImageView) findViewById(R.id.btn_back_to_main);
        mBackMain.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back_to_main:
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("stuid",stuid);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }

    }
}
