package com.example.daireker.dormitory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daireker on 2017/11/30.
 */

public class Query extends AppCompatActivity implements View.OnClickListener{

    private static final int UPDATE_BED_SUCCESS = 1;

    private static final int UPDATE_BED_LOSE = 2;

    private ImageView mBackChoose, mUpdateBed;

    private TextView tx_five, tx_eight, tx_nine, tx_thirteen, tx_fourteen;

    private String stuid, gender, vcode;

    private int fiveBed, eightBed, nineBed, thirteenBed, fourteenBed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_bed_number);

        Intent i = getIntent();
        stuid = i.getStringExtra("stuid");
        gender = i.getStringExtra("gender");
        vcode = i.getStringExtra("vcode");
        Log.d("Query","gender = " + gender);
        initView();
        queryBed(gender);
    }

    void initView(){
        tx_five = (TextView) findViewById(R.id.five_bed);
        tx_eight = (TextView) findViewById(R.id.eight_bed);
        tx_nine = (TextView) findViewById(R.id.nine_bed);
        tx_thirteen = (TextView) findViewById(R.id.thirteen_bed);
        tx_fourteen = (TextView) findViewById(R.id.fourteen_bed);

        mBackChoose = (ImageView) findViewById(R.id.btn_back_to_choose);
        mBackChoose.setOnClickListener(this);

        mUpdateBed = (ImageView) findViewById(R.id.btn_updata_bed);
        mUpdateBed.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back_to_choose:
                Intent i = new Intent(this,ChooseDormitory.class);
                i.putExtra("stuid",stuid);
                i.putExtra("gender",gender);
                i.putExtra("vcode",vcode);
                startActivity(i);
                finish();
                break;
            case R.id.btn_updata_bed:
                Intent j = new Intent(this,Query.class);
                j.putExtra("stuid",stuid);
                j.putExtra("gender",gender);
                j.putExtra("vcode",vcode);
                startActivity(j);
                finish();
                break;
            default:
                break;
        }
    }

    private void queryBed(String gender){
        String data = "gender=" + gender;
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?" + data;
        Log.d("Query",address);
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
                        Log.d("Query",bufferStr);
                        getBedJson(bufferStr);
                    }else{
                        Log.d("Query","连接失败");
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
            Log.d("Query","JsonData = " + JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            int errcode = jsonObject.getInt("errcode");
            Log.d("Query","errcode = " + errcode);

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
            mHandler.sendMessage(msg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateBed(int fiveBed, int eightBed, int nineBed,
                           int thirteenBed, int fourteenBed){
        tx_five.setText(fiveBed + "");
        tx_eight.setText(eightBed + "");
        tx_nine.setText(nineBed + "");
        tx_thirteen.setText(thirteenBed + "");
        tx_fourteen.setText(fourteenBed + "");
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            Log.d("Detail","handler启动");
            switch (msg.what){
                case UPDATE_BED_SUCCESS:
                    ToastUtil.showToast(Query.this,"更新床位信息成功!");
                    updateBed(fiveBed, eightBed, nineBed, thirteenBed, fourteenBed);
                    break;
                case UPDATE_BED_LOSE:
                    ToastUtil.showToast(Query.this,"更新失败!");
                    break;
                default:
                    break;
            }
        }
    };
}
