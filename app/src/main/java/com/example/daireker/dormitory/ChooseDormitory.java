package com.example.daireker.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daireker on 2017/11/7.
 */

public class ChooseDormitory extends AppCompatActivity implements View.OnClickListener{

    private static final int UPDATE_BED_SUCCESS = 1;

    private static final int UPDATE_BED_LOSE = 2;

    private Spinner mPeopleNum, mBuildingNum;

    private ImageView mBackMain;

    private String[] mPeopleStr = {"单人","两人","三人","四人","五人"};

    private String[] mBuildingStr = {"5号楼","8号楼","9号楼","13号楼","14号楼"};

    private String stuid, vcode, gender, building, room;

    private int fiveBed, eightBed, nineBed, thirteenBed, fourteenBed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_dormitory);

        initView();
        Intent i = getIntent();
        stuid = i.getStringExtra("stuid");
        vcode = i.getStringExtra("vcode");
        gender = i.getStringExtra("gender");
        queryBed(gender);
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
                i.putExtra("stuid",stuid);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
    }

    private void queryBed(String gender){
        String data = "gender=" + gender;
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?" + data;
        Log.d("Dormitory",address);
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
                        Log.d("Dormitory",bufferStr);
                        getBedJson(bufferStr);
                    }else{
                        Log.d("Dormitory","连接失败");
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

    private void getBedJson(String JsonData){
        try{
            Log.d("Dormitory","JsonData = " + JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            int errcode = jsonObject.getInt("errcode");
            Log.d("Detail","errcode = " + errcode);

            Message msg = new Message();
            if(errcode == 0){
                JSONObject bedObject = jsonObject.getJSONObject("data");
                fiveBed = bedObject.getInt("5");
                eightBed = bedObject.getInt("8");
                nineBed = bedObject.getInt("9");
                thirteenBed = bedObject.getInt("13");
                fourteenBed = bedObject.getInt("14");

                msg.what = UPDATE_BED_SUCCESS;
            }else{
                msg.what = UPDATE_BED_LOSE;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateBed(int fiveBed, int eightBed, int nineBed,
                           int thirteenBed, int fourteenBed){
        // 5,8,9,13,14剩余床位
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            Log.d("Detail","handler启动");
            switch (msg.what){
                case UPDATE_BED_SUCCESS:
                    ToastUtil.showToast(ChooseDormitory.this,"更新床位信息成功!");
                    updateBed(fiveBed, eightBed, nineBed, thirteenBed, fourteenBed);
                    break;
                case UPDATE_BED_LOSE:
                    ToastUtil.showToast(ChooseDormitory.this,"更新失败!");
                    break;
                default:
                    break;
            }
        }
    };

}
