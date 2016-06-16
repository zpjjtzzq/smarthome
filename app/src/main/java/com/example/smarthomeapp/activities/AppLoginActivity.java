package com.example.smarthomeapp.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.activities.xuhong.UIMainActivity;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.Room;
import com.example.smarthomeapp.result_jzp.AppliancesUpdateResolver;
import com.example.smarthomeapp.util.GsonUtil;
import com.google.gson.Gson;

public class AppLoginActivity extends Activity implements OnClickListener {
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;
    private Button createButton;
    private ActionBar actionBar;
    private TextView actionBarTextView;
    private ProgressDialog loginProgress;
    private Integer user_id;


    public String serverUrl = "http://192.168.1.39:8080/smarthome/userInfo/UserLogin";

    private ArrayList<String> name_pwd = new ArrayList<String>();


    private void initActionBar(){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.item_actionbar);
        actionBarTextView = (TextView) findViewById(R.id.txt_hint_title);
        actionBarTextView.setText(R.string.app_login);
        ImageButton button = (ImageButton) findViewById(R.id.btn_back);

        button.setImageDrawable(getResources().getDrawable(R.drawable.icon_smarthome));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);
        initViews();
        initActionBar();
    }
    private void initViews() {
        loginUsername = (EditText)findViewById(R.id.login_username);
        loginPassword = (EditText)findViewById(R.id.login_password);
        loginButton   = (Button)findViewById(R.id.login);
        createButton  = (Button)findViewById(R.id.create_count);

        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        actionBar = getActionBar();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login:
                handleLogin();
                break;
            case R.id.create_count:
                handleCreateCount();
                break;
            default:
                break;
        }

    }
    private void handleLogin() {
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        login(username, password);
    }
    Activity activity = this;
    private void login(final String username, final String password) {
        loginProgress = new ProgressDialog(this);
        loginProgress.setCancelable(true);
        loginProgress.setCanceledOnTouchOutside(true);
        loginProgress.setMessage("登陆中...");
        loginProgress.show();

        HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {
            @Override
            public void processing(int status, String responsString) {
                loginProgress.dismiss();
                if(status == 201){
                    try {
                        JSONObject jsonObject = new JSONObject(responsString);
                        int result = jsonObject.getInt("result_code");
                        switch (result){
                            case 0 : {
                                Toast.makeText(activity, "登陆成功！", Toast.LENGTH_LONG).show();
                                user_id = jsonObject.getInt("user_id");
                                Log.d("Login", "user_id: " + String.valueOf(user_id));
                                Log.d("login", user_id.toString());
                                onLoginSuccess(user_id);
                                break;
                            }
                            case 1 : {
                                Toast.makeText(activity, "用户名或密码错误！", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 2 : {
                                Toast.makeText(activity, "服务器错误！", Toast.LENGTH_LONG).show();
                                break;
                            }
                            default: {
                                Toast.makeText(activity, "其他错误", Toast.LENGTH_LONG).show();
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        name_pwd.clear();
        name_pwd.add(username);
        name_pwd.add(password);
        AppliancesUpdateResolver.app_login(activity, name_pwd, httpResultProcessListener);

    }
    private void handleCreateCount() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLoginSuccess(final Integer user_id) {
        HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {
            @Override
            public void processing(int status, String responsString) {
                if(status == 201){
                    Gson gson = GsonUtil.create();
                    Log.d("login", responsString);
                    Room[] rooms = gson.fromJson(responsString, Room[].class);
                    Log.d("Login", "rooms: " + String.valueOf(rooms.length));
                    if(rooms == null || rooms.length == 0){
                        Intent intent = new Intent(activity, AddRoomActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                    }
                    if(rooms.length != 0){
                        Intent intent = new Intent(activity, UIMainActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                    }
                }
            }
        };
        AppliancesUpdateResolver.query_room(activity, user_id, httpResultProcessListener);
    }
}
