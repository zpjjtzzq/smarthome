package com.example.smarthomeapp.result_jzp;

import android.content.Context;

import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.listener.ReflectionUtil;
import com.example.smarthomeapp.util.AsyncHttpUtil;

import java.util.Set;

/**
 * Created by 卧龙风 on 2015/8/5.
 */
public class WeatherQueryResolver implements HttpResultProcessListener {


    public static void queryWeatherData(Context context,
                                        HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/weather/hefeng";
        AsyncHttpUtil.requestJsonWeather(context, url, listener);
    }

    @Override
    public void processing(int status, String responsString) {
        ReflectionUtil reflectionUtil = new ReflectionUtil(responsString);
        reflectionUtil.logResponseData(WeatherQueryResolver.class);
    }
}
