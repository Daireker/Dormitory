package com.example.daireker.dormitory;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int UPDATE_DETAIL_SUCCESS = 1;

    private static final int UPDATE_DETAIL_LOSE = 2;

    private ImageView mBack, mMap , mShare, mUpdate;

    private TextView mName, mStuNum, mSex, mCondition, mBuildNum, mDorNum;

    String name, studentid, gender, room, building, vcode, location, grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        Intent i = getIntent();
        String stuid = i.getStringExtra("stuid");
        queryStuDatail(stuid);
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
                j.putExtra("stuid",studentid);
                startActivity(j);
                finish();
                break;
            case R.id.tx_condition:
                if(mCondition.getText().toString().equals("点击办理")){
                    Intent k = new Intent(this,ChooseDormitory.class);
                    k.putExtra("stuid",studentid);
                    k.putExtra("vcode",vcode);
                    k.putExtra("gender",gender);
                    startActivity(k);
                    finish();
                }
                break;
            case R.id.btn_share:
                ToastUtil.showToast(MainActivity.this,"To Be Continue...");
                break;
            case R.id.btn_update:
                Intent q = new Intent(this,MainActivity.class);
                q.putExtra("stuid",studentid);
                startActivity(q);
                finish();
                break;
            default:
                break;
        }
    }

    private void queryStuDatail(String stuid){
        String data = "stuid=" + stuid;
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?" + data;
        Log.d("Detail",address);
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
                        Log.d("Detail",bufferStr);
                        getDetailJson(bufferStr);
                    }else{
                        Log.d("Detail","连接失败");
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

    private void getDetailJson(String JsonData){
        try {
            Log.d("Detail","JsonData = " + JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            int errcode = jsonObject.getInt("errcode");
            Log.d("Detail","errcode = " + errcode);

            Message msg = new Message();
            if(errcode == 0){
                JSONObject detailObject = jsonObject.getJSONObject("data");
                studentid = detailObject.getString("studentid");
                Log.d("Detail","studentid = " + studentid);
                name = detailObject.getString("name");
                Log.d("Detail","name = " + name);
                gender = detailObject.getString("gender");
                Log.d("Detail","gender = " + gender);
                vcode = detailObject.getString("vcode");
                Log.d("Detail","vcode = " + vcode);
//                room = detailObject.getString("room");
//                Log.d("Detail","room = " + room);
//                building = detailObject.getString("building");
//                Log.d("Detail","building = " + building);
                location = detailObject.getString("location");
                Log.d("Detail","location = " + location);
                grade = detailObject.getString("grade");
                Log.d("Detail","grade = " + grade);

                msg.what = UPDATE_DETAIL_SUCCESS;
            }else{
                msg.what = UPDATE_DETAIL_LOSE;
            }
            mHandler.sendMessage(msg);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateDetail(String name,String studentid,String gender){
        Log.d("Detail","更新成功");
        mName.setText(name);
        mStuNum.setText(studentid);
        mSex.setText(gender);
        //mBuildNum.setText(building);
        //mDorNum.setText(room);
        mBuildNum.setText("未办理");
        mDorNum.setText("未办理");

        int StuNum = Integer.parseInt(studentid);
        if(StuNum%2 == 0){
            mCondition.setText("已办理");
        }else {
            mCondition.setText("点击办理");
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            Log.d("Detail","handler启动");
            switch (msg.what){
                case UPDATE_DETAIL_SUCCESS:
                    ToastUtil.showToast(MainActivity.this,"更新学生信息成功!");
                    updateDetail(name, studentid, gender);
                    break;
                case UPDATE_DETAIL_LOSE:
                    ToastUtil.showToast(MainActivity.this,"更新失败!");
                    break;
                default:
                    break;
            }
        }
    };
}
