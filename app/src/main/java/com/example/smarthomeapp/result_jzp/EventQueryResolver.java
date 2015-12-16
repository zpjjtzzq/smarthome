package com.example.smarthomeapp.result_jzp;

import android.content.Context;

import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.listener.ReflectionUtil;
import com.example.smarthomeapp.util.AsyncHttpUtil;


/**
 * Created by 卧龙风 on 2015/8/5.
 */
public class EventQueryResolver implements HttpResultProcessListener {

    public static void queryWearableSensorData(Context context, HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/wearableInfo/query";
        AsyncHttpUtil.requestJsonWeather(context, url, listener);
    }

    public static void queryGpsData(Context context, HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/gpsInfo/query";
        AsyncHttpUtil.requestJsonWeather(context,url,listener);
    }

    public static void querySocialData(Context context, HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/socialInfo/query";
        AsyncHttpUtil.requestJsonWeather(context,url,listener);
    }



    @Override
    public void processing(int status, String responsString){
        ReflectionUtil reflectionUtil = new ReflectionUtil(responsString);
        reflectionUtil.logResponseData(SensorQueryResolver.class);
    }
}

