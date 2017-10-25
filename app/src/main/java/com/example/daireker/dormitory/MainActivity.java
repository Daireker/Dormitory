package com.example.daireker.dormitory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        mBack = (Button) findViewById(R.id.back);
        mBack.setOnClickListener(this);

    }

    void initView(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                Intent i = new Intent(this,Login.class);
                finish();
                break;
            default:
                break;
        }

    }
}
