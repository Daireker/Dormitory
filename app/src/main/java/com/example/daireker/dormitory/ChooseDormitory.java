package com.example.daireker.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by daireker on 2017/11/7.
 */

public class ChooseDormitory extends AppCompatActivity implements View.OnClickListener{

    private Spinner mPeopleNum, mBuildingNum;

    private ImageView mBackMain;

    private String[] mPeopleStr = {"单人","两人","三人","四人","五人"};

    private String[] mBuildingStr = {"5号楼","8号楼","9号楼","13号楼","14号楼"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_dormitory);
        initView();
    }

    void initView(){
        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner_text_item,mPeopleStr);
        //peopleAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        peopleAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        mPeopleNum = (Spinner) findViewById(R.id.choose_people_number);
        mPeopleNum.setAdapter(peopleAdapter);
        mPeopleNum.setOnItemSelectedListener(new ItemSelectedListener());
        mPeopleNum.setVisibility(View.VISIBLE);

        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner_text_item,mBuildingStr);
        //buildingAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        buildingAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        mBuildingNum = (Spinner) findViewById(R.id.choose_buliding_number);
        mBuildingNum.setAdapter(buildingAdapter);
        mBuildingNum.setOnItemSelectedListener(new ItemSelectedListener());

        mBackMain = (ImageView) findViewById(R.id.btn_choose_back_to_main);
        mBackMain.setOnClickListener(this);
    }

    private class ItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_people_number:
                break;
            case R.id.choose_buliding_number:
                break;
            case R.id.btn_choose_back_to_main:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
    }
}
