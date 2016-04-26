package com.example.smarthomeapp.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.UserInfo;
import com.example.smarthomeapp.result_jzp.AppliancesUpdateResolver;

public class CreateUserActivity extends Activity implements OnClickListener {


    private EditText eUsername;
    private EditText ePwd1;
    private EditText ePwd2;
    private RadioGroup rGender;
    private EditText eAge;
    private EditText ePhone;
    private EditText eEmail;

    private Button btnSubmit;
    private Button btnReset;

    ProgressDialog progress;

    private ActionBar actionBar;
    private TextView actionBarTextView;


    private void initActionBar(){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.item_actionbar);
        actionBarTextView = (TextView) findViewById(R.id.txt_hint_title);
        actionBarTextView.setText(R.string.app_login);
        ImageButton button = (ImageButton) findViewById(R.id.btn_back);

        button.setImageDrawable(getResources().getDrawable(R.drawable.icon_smarthome));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        initViews();
        initActionBar();
    }

    private void initViews() {
        eUsername = (EditText)findViewById(R.id.new_username);
        ePwd1 = (EditText)findViewById(R.id.new_password_1);
        ePwd2 = (EditText)findViewById(R.id.new_password_2);
        rGender = (RadioGroup)findViewById(R.id.new_radio_group_gender);
        eAge = (EditText)findViewById(R.id.new_age);
        ePhone = (EditText)findViewById(R.id.new_phone);
        eEmail = (EditText)findViewById(R.id.new_email);
        btnSubmit = (Button)findViewById(R.id.new_btn_submit);
        btnReset = (Button)findViewById(R.id.new_btn_reset);
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        actionBar = getActionBar();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.new_btn_submit:
                handleCreateAccount();
                break;
            case R.id.new_btn_reset:
                handleReset();
                break;
        }

    }

    private void handleCreateAccount() {
        boolean isUsernameValid = checkUsername();
        if(!isUsernameValid) {
            Toast.makeText(this, "用户名不正确，请重新输入", Toast.LENGTH_LONG).show();
            return;
        }

        int pwdResult = checkPassword();
        if(pwdResult == 1) {
            Toast.makeText(this, "两次输入的密码不一致，请确认！", Toast.LENGTH_LONG).show();
            return;
        }
        if (pwdResult == 2) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        int isAgeValid = checkAge();
        if(isAgeValid == -1) {
            Toast.makeText(this, "年龄不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        if(isAgeValid == -2) {
            Toast.makeText(this, "年龄超出范围(1~100)！", Toast.LENGTH_LONG).show();
            return;
        }
        if(isAgeValid == -3) {
            Toast.makeText(this, "年龄格式输入错误，请不要输入字母、符号等其他字符串！", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(ePhone.getText().toString())) {
            Toast.makeText(this, "请输入电话号码！", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(eEmail.getText().toString())) {
            Toast.makeText(this, "请输入邮箱！", Toast.LENGTH_LONG).show();
            return;
        }

        createAccount();
    }

    Activity activity = this;
    Context context = this;
    private void createAccount() {
        final UserInfo userInfo = new UserInfo();
        userInfo.setAge(eAge.getText().toString());
        userInfo.setEmail(eAge.getText().toString());
        RadioButton selectedGender = (RadioButton)CreateUserActivity.this.findViewById(rGender.getCheckedRadioButtonId());
        userInfo.setGender(selectedGender.getText().toString());
        userInfo.setPassword(ePwd1.getText().toString());
        userInfo.setUsername(eUsername.getText().toString());
        userInfo.setPhone(ePhone.getText().toString());
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Regist...");
        progress.show();
        final HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {
            @Override
            public void processing(int status, String responsString) {
                progress.dismiss();
                if(status == 201){
                    Log.d("create_user", "success");
                    try {
                        JSONObject jsonObject = new JSONObject(responsString);
                        int result = jsonObject.getInt("result_code");
                        switch (result){
                            case 0 : {
                                Toast.makeText(context, "Success!",Toast.LENGTH_LONG ).show();
                                Intent intent  = new Intent(activity, AppLoginActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case 1 : {
                                Toast.makeText(context, "chong fu", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case 2 : {
                                Toast.makeText(context, "Database Error!", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        AppliancesUpdateResolver.create_user(activity, userInfo, httpResultProcessListener);
    }

    private boolean checkUsername() {
        String username = eUsername.getText().toString();
        if(TextUtils.isEmpty(username)) {
            return false;
        }
        return true;
    }

    private int checkPassword() {
		/*
		 * return value:
		 * 0 password valid
		 * 1 password not equal 2 inputs
		 * 2 password empty
		 * */
        String pwd1 = ePwd1.getText().toString();
        String pwd2 = ePwd2.getText().toString();
        if(!pwd1.equals(pwd2)) {
            return 1;
        } else if(TextUtils.isEmpty(pwd1)) {
            return 2;
        } else {
            return 0;
        }
    }

    private int checkAge() {
		/*
		 * return value
		 * 0 输入合法
		 * -1 输入为空
		 * -2输入为负数
		 * -3输入为非数值字符串或包括小数
		 * */
        int ageNum;
        String age = eAge.getText().toString();
        if(TextUtils.isEmpty(age)) {
            return -1;
        }
        try {
            ageNum = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -3;
        }
        if(ageNum <= 0 || ageNum > 100) {
            return -2;
        }
        return 0;
    }

    private void handleReset() {
        eUsername.setText("");
        ePwd1.setText("");
        ePwd2.setText("");
        ((RadioButton)(rGender.getChildAt(0))).setChecked(true);
        eAge.setText("");
        ePhone.setText("");
        eEmail.setText("");
    }


}
