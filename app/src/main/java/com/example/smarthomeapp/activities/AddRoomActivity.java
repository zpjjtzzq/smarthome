package com.example.smarthomeapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.activities.xuhong.UIMainActivity;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.Room;
import com.example.smarthomeapp.model.UserInfo;
import com.example.smarthomeapp.result_jzp.AppliancesUpdateResolver;

import org.json.JSONObject;

public class AddRoomActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    private TextView textView_room_name;
    private TextView textView_device_list;
    private EditText editText_new_room;
    private CheckBox checkBox_aircondition;
    private CheckBox checkBox_water_heater;
    private CheckBox checkBox_lamp;
    private CheckBox checkBox_plug;
    private CheckBox checkBox_curtain;
    private ActionBar actionBar;
    private TextView actionBarTextView;
    private Button add_room;
    private Integer user_id;
    private Room room;
    ProgressDialog progress;
    private Activity activity;
    private UserInfo userInfo;

    private void initActionBar(){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.item_actionbar);
        actionBarTextView = (TextView) findViewById(R.id.txt_hint_title);
        actionBarTextView.setText(R.string.add_room);
        ImageButton button = (ImageButton) findViewById(R.id.btn_back);
        button.setImageDrawable(getResources().getDrawable(R.drawable.icon_smarthome));
    }

    private  void initView(){
        editText_new_room = (EditText)findViewById(R.id.new_room);
        checkBox_aircondition = (CheckBox)findViewById(R.id.cb_air_condition);
        checkBox_water_heater = (CheckBox) findViewById(R.id.cb_water_heater);
        checkBox_curtain  = (CheckBox) findViewById(R.id.cb_curtain);
        checkBox_lamp = (CheckBox) findViewById(R.id.cb_lamp);
        checkBox_plug = (CheckBox) findViewById(R.id.cb_plug);
        add_room = (Button) findViewById(R.id.bnt_add_room);
        add_room.setOnClickListener(this);
        actionBar = getActionBar();
        userInfo = new UserInfo();
        room = new Room();
        Log.d("add_room", "initView");
    }

    private void handle_add_room(){
        boolean is_valid_room_name = check_room_name();
        if(!is_valid_room_name){
            Toast.makeText(this, "输入的房间名字不正确", Toast.LENGTH_LONG).show();
            return;
        }
        room.setRoomName(editText_new_room.getText().toString());
        Log.d("add_room", room.getRoomName());
        create_room();
        // 网络通信
    }

    private void handleHttpResult(JSONObject obj){
        try {
            int result = obj.getInt("result_code");
            switch (result) {
                case 0: {
                    Toast.makeText(activity, "创建成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, UIMainActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {
        @Override
        public void processing(int status, String responsString) {
            if(status == 201){
                progress.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(responsString);
                    handleHttpResult(jsonObject);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };
    private void create_room(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("新建房间中...");
        progress.show();
        AppliancesUpdateResolver.create_room(new Activity(), room, httpResultProcessListener);
    }

    boolean check_room_name(){
        String room_name = editText_new_room.getText().toString();
        if(TextUtils.isEmpty(room_name)){
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        initView();
        initActionBar();
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 2);
        Log.d("add_room", user_id.toString());
        userInfo.setUserId(user_id);
        room.setUserInfo(userInfo);
        Log.d("add_room", "onCreate");
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_room, menu);
        return true;
    }*/

    @Override
    public  void onCheckedChanged(CompoundButton arg0, boolean arg1){
        switch (arg0.getId()){
            case R.id.cb_air_condition:
                if(arg1)
                    room.setIsExistedAirCondition(true);
                else
                    room.setIsExistedAirCondition(false);
                break;
            case R.id.cb_curtain:
                if(arg1)
                    room.setIsExistedCurtain(true);
                else
                    room.setIsExistedCurtain(false);
                break;
            case R.id.cb_lamp:
                if(arg1)
                    room.setIsExistedLamp(true);
                else
                    room.setIsExistedLamp(false);
                break;
            case R.id.cb_water_heater:
                if(arg1)
                    room.setIsExistedWaterHeater(true);
                else
                    room.setIsExistedWaterHeater(false);
                break;
            case R.id.cb_plug:
                if(arg1)
                    room.setIsExistedSheSwitch(true);
                else
                    room.setIsExistedSheSwitch(false);
                break;
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bnt_add_room:
                handle_add_room();
                break;
        }
    }
}
