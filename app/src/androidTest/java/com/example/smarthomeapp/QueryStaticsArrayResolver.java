package com.example.smarthomeapp;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.test.ApplicationTestCase;

import com.example.smarthomeapp.result_jzp.StaticsDataQueryResolver;

/**
 * Created by 卧龙风 on 2015/8/7.
 */
public class QueryStaticsArrayResolver extends ApplicationTestCase<Application> {
    public QueryStaticsArrayResolver() {
        super(Application.class);
    }

    public void testplrArrayDatas() {
        Context context = getContext();
        StaticsDataQueryResolver staticsDataQueryResolver = new StaticsDataQueryResolver();
        staticsDataQueryResolver.queryPlrArrayDatas(context, staticsDataQueryResolver);
        Looper.loop();
    }

    public void testtemperatureArrayDatas() {
        Context context = getContext();
        StaticsDataQueryResolver staticsDataQueryResolver = new StaticsDataQueryResolver();
        staticsDataQueryResolver.queryTemperatureArrayDatas(context, staticsDataQueryResolver);
        Looper.loop();
    }

    public void testpriceArrayDatas() {
        Context context = getContext();
        StaticsDataQueryResolver staticsDataQueryResolver = new StaticsDataQueryResolver();
        staticsDataQueryResolver.queryPriceDatas(context, staticsDataQueryResolver);
        Looper.loop();
    }

    public void testmeterArrayDatas() {
        Context context = getContext();
        StaticsDataQueryResolver staticsDataQueryResolver = new StaticsDataQueryResolver();
        StaticsDataQueryResolver.queryMeterArrayDatas(context, staticsDataQueryResolver);
        Looper.loop();
    }
}
