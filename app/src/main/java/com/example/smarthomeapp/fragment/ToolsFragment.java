package com.example.smarthomeapp.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import com.example.smarthomeapp.R;
import com.example.smarthomeapp.activities.WBStatusAPIActivity;
import com.example.smarthomeapp.activities.xuhong.UIMainActivity;
import com.example.smarthomeapp.services.CollectDataService;
import com.example.smarthomeapp.thirdparty.sina.GetCalendarEvent;
import com.example.smarthomeapp.thirdparty.sina.GetSMS;
import com.example.smarthomeapp.util.AsyncHttpUtil;


public class ToolsFragment extends PreferenceFragment  implements Preference.OnPreferenceClickListener {
    UIMainActivity uiMainActivity;
    private CheckBoxPreference apply_gpsPreference;
    private CheckBoxPreference apply_socialPreferene;
    private EditTextPreference set_homeaddPreference;
    private ListPreference set_gps_time;
    private ListPreference set_weibo_time;
    private ListPreference set_message_time;
    private ListPreference set_carlender_time;

    private SharedPreferences sharedPreferences;
    private  CollectDataService collectDataService;
    private ServiceConnection connection = new ServiceConnection() {
        //获取服务对象时的操作
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            collectDataService = ((CollectDataService.ServiceBinder)iBinder).getService();
        }

        //无法获取服务对象时的操作
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            collectDataService = null;

        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        sharedPreferences = this.getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE);

        apply_gpsPreference = (CheckBoxPreference) findPreference("gps_set_preference");
        apply_socialPreferene = (CheckBoxPreference) findPreference("social_set_preference");
        set_homeaddPreference = (EditTextPreference) findPreference("homeaddress_set_preference");
        set_gps_time = (ListPreference) findPreference("gps_time_frequency_setting");
        set_weibo_time = (ListPreference) findPreference("weibo_time_frequency_setting");
        set_message_time = (ListPreference) findPreference("message_time_frequency_setting");
        set_carlender_time = (ListPreference) findPreference("carlender_time_frequency_setting");

        apply_gpsPreference.setOnPreferenceClickListener(this);
        apply_socialPreferene.setOnPreferenceClickListener(this);
        set_homeaddPreference.setOnPreferenceClickListener(this);
        set_gps_time.setOnPreferenceClickListener(this);
        set_weibo_time.setOnPreferenceClickListener(this);
        set_message_time.setOnPreferenceClickListener(this);
        set_carlender_time.setOnPreferenceClickListener(this);



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        uiMainActivity= ((UIMainActivity)activity);
        Intent intent = new Intent(uiMainActivity, CollectDataService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent,connection,Context.BIND_AUTO_CREATE);

    }

    private void operatePreference(Preference preference) {
        if (preference == apply_gpsPreference) {
            if (apply_gpsPreference.isChecked() == true) {
//                getGpsState(this.getActivity());
            }else{
//                turnGpsOff(this.getActivity());
            }

            Log.i("hello preference", "gps CB, and isChecked = " + apply_gpsPreference.isChecked());

            } else
            if (preference == apply_socialPreferene) {
                if (apply_socialPreferene.isChecked() == true) {
                    Intent it = new Intent(this.getActivity(),
                            WBStatusAPIActivity.class);
                    this.getActivity().startActivityForResult(it, 0);
                }
                Log.i("hello preference", "weibo CB ,and isChecked =" + apply_socialPreferene.isChecked());

            } else if (preference == set_homeaddPreference) {
                Log.i("hello preference", "val" +
                        "ue" + set_homeaddPreference.getText());
                String homeaddress;
                if (set_homeaddPreference.getText() == null) {
                    homeaddress = "西安市碑林区咸宁西路28号西安交通大学";
                } else {
                    homeaddress = set_homeaddPreference.getText();
                }
                String url = "http://202.117.14.247:8080/smarthome/gpsInfo/saveUserAddress";
                AsyncHttpUtil.requestJson(this.getActivity(), url, homeaddress, null);
            } else if (preference == set_gps_time) {
                Log.i("hello preference", "gps_set_time CB,and selectValue =" +
                        set_gps_time.getValue() + ",Text =" + set_gps_time.getEntry());

                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (set_gps_time.getValue()) {
                    case "0":
                        editor.putInt("TimerTaskInterval", 30000);
                        editor.commit();
                        collectDataService.setGpsInterval(sharedPreferences
                                .getInt("GpsInterval", 30000));
                        break;
                    case "1":
                        editor.putInt("TimerTaskInterval", 300000);
                        editor.commit();
                        collectDataService.setGpsInterval(sharedPreferences
                                .getInt("GpsInterval", 300000));
                        break;
                    case "2":
                        editor.putInt("TimerTaskInterval", 1200000);
                        editor.commit();
                        collectDataService.setGpsInterval(sharedPreferences
                                .getInt("GpsInterval", 1200000));
                        break;
                    case "3":
                        editor.putInt("TimerTaskInterval", 3600000);
                        editor.commit();
                        collectDataService.setGpsInterval(sharedPreferences
                                .getInt("GpsInterval", 360000));
                        break;

                }

                Toast.makeText(this.getActivity(),"频率设置成功",Toast.LENGTH_SHORT).show();

            } else if (preference == set_message_time) {
                Log.i("hello preference", "set_message_time CB,and selectValue =" +
                        set_message_time.getValue() + ",Text =" + set_message_time.getEntry());

                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (set_message_time.getValue()) {
                    case "0":
                        editor.putInt("TimerTaskInterval", 30000);
                        editor.commit();
                        GetSMS.setInterval(sharedPreferences.getInt("TimerTaskInterval", 30000));
                        collectDataService.restartSmsTimer();
                        break;
                    case "1":
                        editor.putInt("TimerTaskInterval", 300000);
                        editor.commit();
                        GetSMS.setInterval(sharedPreferences.getInt("TimerTaskInterval", 300000));
                        collectDataService.restartSmsTimer();
                        break;
                    case "2":
                        editor.putInt("TimerTaskInterval", 1200000);
                        editor.commit();
                        GetSMS.setInterval(sharedPreferences.getInt("TimerTaskInterval", 1200000));
                        collectDataService.restartSmsTimer();
                        break;
                    case "3":
                        editor.putInt("TimerTaskInterval", 3600000);
                        editor.commit();
                        GetSMS.setInterval(sharedPreferences.getInt("TimerTaskInterval", 3600000));
                        collectDataService.restartSmsTimer();
                        break;

                }

                Toast.makeText(this.getActivity(),"频率设置成功",Toast.LENGTH_SHORT).show();


            } else if (preference == set_weibo_time) {
                Log.i("hello preference", "set_weibo_time CB,and selectValue =" +
                        set_weibo_time.getValue() + ",Text =" + set_weibo_time.getEntry());

                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (set_message_time.getValue()) {
                    case "0":
                        editor.putInt("TimerTaskInterval", 30000);
                        editor.commit();
                        collectDataService.restartWeiBoEventTimer(30000);
                        break;
                    case "1":
                        editor.putInt("TimerTaskInterval", 300000);
                        editor.commit();
                        collectDataService.restartWeiBoEventTimer(300000);
                        break;
                    case "2":
                        editor.putInt("TimerTaskInterval", 1200000);
                        editor.commit();
                        collectDataService.restartWeiBoEventTimer(1200000);
                        break;
                    case "3":
                        editor.putInt("TimerTaskInterval", 3600000);
                        editor.commit();
                        collectDataService.restartWeiBoEventTimer(3600000);
                        break;
                }

                Toast.makeText(this.getActivity(),"频率设置成功",Toast.LENGTH_SHORT).show();

            } else if (preference == set_carlender_time) {
                Log.i("hello preference", "set_carlender_time CB,and selectValue =" +
                        set_carlender_time.getValue() + ",Text =" + set_carlender_time.getEntry());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (set_message_time.getValue()) {
                    case "0":
                        editor.putInt("TimerTaskInterval", 30000);
                        editor.commit();
                        GetCalendarEvent.setInterval(sharedPreferences.getInt("TimerTaskInterval", 30000));
                        collectDataService.restartCalendarTimer();
                        break;
                    case "1":
                        editor.putInt("TimerTaskInterval", 300000);
                        editor.commit();
                        GetCalendarEvent.setInterval(sharedPreferences.getInt("TimerTaskInterval", 300000));
                        collectDataService.restartCalendarTimer();
                        break;
                    case "2":
                        editor.putInt("TimerTaskInterval", 1200000);
                        editor.commit();
                        GetCalendarEvent.setInterval(sharedPreferences.getInt("TimerTaskInterval", 1200000));
                        collectDataService.restartCalendarTimer();
                        break;
                    case "3":
                        editor.putInt("TimerTaskInterval", 3600000);
                        editor.commit();
                        GetCalendarEvent.setInterval(sharedPreferences.getInt("TimerTaskInterval", 3600000));
                        collectDataService.restartCalendarTimer();
                        break;
                }


                Toast.makeText(this.getActivity(),"频率设置成功",Toast.LENGTH_SHORT).show();

            }
        }



    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i("hello preference", "onPreferenceClick----->" + String.valueOf(preference.getKey()));
        operatePreference(preference);
        return true;
    }



    private static void getGpsState(Context context){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        }

    private void turnGpsOff(Context context){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled",false);
        context.sendBroadcast(intent);
    }

}
