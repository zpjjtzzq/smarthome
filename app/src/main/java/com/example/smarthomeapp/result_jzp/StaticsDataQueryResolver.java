package com.example.smarthomeapp.result_jzp;

import android.content.Context;

import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.listener.ReflectionUtil;
import com.example.smarthomeapp.util.AsyncHttpUtil;

import java.util.Set;

/**
 * Created by 卧龙风 on 2015/8/7.
 */
public class StaticsDataQueryResolver  implements HttpResultProcessListener {

    public static void queryTemperatureArrayDatas(Context context,
                                                  HttpResultProcessListener listener) {
        String url = "http://202.117.14.247:8080/smarthome/sensorBox/temperatureEveryTwenty";
        AsyncHttpUtil.requestJsonWeather(context, url, listener);

    }

    public static void queryPlrArrayDatas(Context context,
                                                  HttpResultProcessListener listener) {
        String url = "http://202.117.14.247:8080/smarthome/sensorBox/plrEveryTwenty";
        AsyncHttpUtil.requestJsonWeather(context, url, listener);

    }

    public static void queryPriceDatas(Context context,
                                       HttpResultProcessListener listener) {
        String url = "http://202.117.14.247:8080/smarthome/price/month";
        AsyncHttpUtil.requestJsonWeather(context, url, listener);

    }

    public static void queryMeterArrayDatas(Context context,
                                            HttpResultProcessListener listener) {
        String url = "http://202.117.14.247:8080/smarthome/smartMeter/meterEveryHour";
        AsyncHttpUtil.requestJsonWeather(context, url, listener);

    }


    @Override
    public void processing(int status, String responsString) {
        ReflectionUtil reflectionUtil = new ReflectionUtil(responsString);
        reflectionUtil.logResponseData(MeterQueryResolver.class);
    }
}
