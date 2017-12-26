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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daireker on 2017/11/7.
 */

public class ChooseDormitory extends AppCompatActivity implements View.OnClickListener{

    private static final int COMMIT_SUCCESS = 1;

    private static final int COMMIT_LOSE = 2;

    private Spinner mPeopleNum, mBuildingNum;

    private ImageView mBackMain;

    private TextView mVcode;

    private Button mQuery, mCommit;

    private String[] mPeopleStr = {"单人","两人","三人","四人"};

    private String[] mBuildingStr = {"5号楼","8号楼","9号楼","13号楼","14号楼"};

    private String stuid, vcode, gender, buildingNum, roomNum, peopleNum;

    private String stu1id, stu2id, stu3id, v1code, v2code, v3code;

    private int needNum ,buildingNumInt;

    private EditText mRoommate_1_stuid, mRoommate_2_stuid, mRoommate_3_stuid, mRoommate_1_vcode, mRoommate_2_vcode, mRoommate_3_vcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_dormitory);

        Intent i = getIntent();
        stuid = i.getStringExtra("stuid");
        vcode = i.getStringExtra("vcode");
        gender = i.getStringExtra("gender");
        Log.d("Choose","vcode = " + vcode);
        initView();
    }

    void initView(){
        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner_text_item,mPeopleStr);
        //peopleAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        peopleAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        mPeopleNum = (Spinner) findViewById(R.id.choose_people_number);
        mPeopleNum.setAdapter(peopleAdapter);
        mPeopleNum.setOnItemSelectedListener(new ItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {
                peopleNum = mPeopleNum.getSelectedItem().toString();
                Log.d("Choose","人数为 " + peopleNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mPeopleNum.setVisibility(View.VISIBLE);

        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner_text_item,mBuildingStr);
        //buildingAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        buildingAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        mBuildingNum = (Spinner) findViewById(R.id.choose_buliding_number);
        mBuildingNum.setAdapter(buildingAdapter);
        mBuildingNum.setOnItemSelectedListener(new ItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {
                buildingNum = mBuildingNum.getSelectedItem().toString();
                Log.d("Choose","楼号为 " + buildingNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBackMain = (ImageView) findViewById(R.id.btn_choose_back_to_main);
        mBackMain.setOnClickListener(this);

        mVcode = (TextView) findViewById(R.id.vcodeView);
        mVcode.setText(vcode + "(共同选宿舍需提供)");

        mQuery = (Button) findViewById(R.id.btn_query);
        mQuery.setOnClickListener(this);

        mCommit = (Button) findViewById(R.id.btn_commit);
        mCommit.setOnClickListener(this);

        mRoommate_1_stuid = (EditText) findViewById(R.id.roommate_1_stuid);
        mRoommate_2_stuid = (EditText) findViewById(R.id.roommate_2_stuid);
        mRoommate_3_stuid = (EditText) findViewById(R.id.roommate_3_stuid);
        mRoommate_1_vcode = (EditText) findViewById(R.id.roommate_1_vcode);
        mRoommate_2_vcode = (EditText) findViewById(R.id.roommate_2_vcode);
        mRoommate_3_vcode = (EditText) findViewById(R.id.roommate_3_vcode);
    }

    private void assignment(){
        stu1id = mRoommate_1_stuid.getText().toString();
        stu2id = mRoommate_2_stuid.getText().toString();
        stu3id = mRoommate_3_stuid.getText().toString();
        v1code = mRoommate_1_vcode.getText().toString();
        v2code = mRoommate_2_vcode.getText().toString();
        v3code = mRoommate_3_vcode.getText().toString();
        if(buildingNum.equals("5号楼")){
            buildingNumInt = 5;
        }else if(buildingNum.equals("8号楼")){
            buildingNumInt = 8;
        }else if(buildingNum.equals("9号楼")){
            buildingNumInt = 9;
        }else if(buildingNum.equals("13号楼")){
            buildingNumInt = 13;
        }else {
            buildingNumInt = 14;
        }

        if(peopleNum.equals("单人")){
            needNum = 1;
        }else if(peopleNum.equals("两人")){
            needNum = 2;
        }else if(peopleNum.equals("三人")){
            needNum = 3;
        }else {
            needNum = 4;
        }
    }

    private class ItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }


    private boolean checkPeopleNum(String stu1id, String stu2id, String stu3id,
                                String v1code, String v2code, String v3code){
        int writeNum = 1;

        if((stu1id != null && stu1id.length() > 0) && (v1code != null && v1code.length() > 0)){
            if(stu1id.equals(stuid)){
                ToastUtil.showToast(ChooseDormitory.this,"同住人间或同住人与自己信息重复！");
                return false;
            }else{
                writeNum += 1;
            }
        }
        if((stu2id != null && stu2id.length() > 0) && (v2code != null && v2code.length() > 0)){
            if(stu2id.equals(stuid) || stu2id.equals(stu1id)){
                ToastUtil.showToast(ChooseDormitory.this,"同住人间或同住人与自己信息重复！");
                return false;
            }else{
                writeNum += 1;
            }
        }
        if((stu3id != null && stu3id.length() > 0) && (v3code != null && v3code.length() > 0)){
            if(stu3id.equals(stuid) || stu3id.equals(stu1id) || stu3id.equals(stu2id)){
                ToastUtil.showToast(ChooseDormitory.this,"同住人间或同住人与自己信息重复！");
                return false;
            }else{
                writeNum += 1;
            }
        }

        Log.d("Choose","needNum = " + needNum);
        Log.d("Choose","writeNum = " + writeNum);
        if(needNum == writeNum){
            return true;
        }else {
            ToastUtil.showToast(ChooseDormitory.this,"所选人数与已填信息不符！");
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_choose_back_to_main:
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("stuid",stuid);
                startActivity(i);
                finish();
                break;
            case R.id.btn_query:
                Intent j = new Intent(this,Query.class);
                j.putExtra("stuid",stuid);
                j.putExtra("gender",gender);
                j.putExtra("vcode",vcode);
                startActivity(j);
                finish();
                break;
            case R.id.btn_commit:
                assignment();
                queryCanCommit(needNum, buildingNumInt, stuid, stu1id, stu2id, stu3id, v1code, v2code, v3code);
                break;
            default:
                break;
        }
    }

    private void queryCanCommit(int needNum, int buildingNumInt, String stuid, String stu1id, String stu2id, String stu3id,
                                String v1code, String v2code, String v3code){
        if(checkPeopleNum(stu1id, stu2id, stu3id, v1code, v2code, v3code)){
            final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
            try{
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("num",needNum);
                jsonObject.put("stuid",stuid);
                jsonObject.put("stu1id",stu1id);
                jsonObject.put("v1code",v1code);
                jsonObject.put("stu2id",stu2id);
                jsonObject.put("v2code",v2code);
                jsonObject.put("stu3id",stu3id);
                jsonObject.put("v3code",v3code);
                jsonObject.put("buildingNo",buildingNumInt);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection con = null;
                        try{
                            URL url = new URL(address);
                            HTTPSTrustManager.allowAllSSL();
                            con = (HttpURLConnection)url.openConnection();
                            con.setDoOutput(true);
                            con.setDoInput(true);
                            con.setRequestMethod("POST");
                            con.setConnectTimeout(8000);
                            con.setReadTimeout(8000);
                            con.setUseCaches(false);
                            con.setInstanceFollowRedirects(true);
                            con.setRequestProperty("Content-Type","application/json");
                            con.connect();

                            DataOutputStream out = new DataOutputStream(con.getOutputStream());
                            out.writeBytes(jsonObject.toString());
                            out.flush();
                            out.close();

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
                                getChooseJson(bufferStr);
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
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else {
            return;
        }
    }

    private void getChooseJson(String JsonData){
        try{
            Log.d("Choose","JsonData = " + JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            int errcode = jsonObject.getInt("errcode");
            Log.d("Choose","errcode = " + errcode);

            Message msg = new Message();
            if(errcode == 0){
                msg.what = COMMIT_SUCCESS;
            }else {
                msg.what = COMMIT_LOSE;
            }
            mHandler.sendMessage(msg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            Log.d("Detail","handler启动");
            switch (msg.what){
                case COMMIT_SUCCESS:
                    ToastUtil.showToast(ChooseDormitory.this,"提交成功!");
                    break;
                case COMMIT_LOSE:
                    ToastUtil.showToast(ChooseDormitory.this,"提交失败!");
                    break;
                default:
                    break;
            }
        }
    };

}
