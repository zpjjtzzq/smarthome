package com.example.smarthomeapp.model.jia;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.model.HumidySensorData;

/**
 * Created by 卧龙风 on 2015/7/25.
 */


public class HumiditySensorAdapter extends SensorDataAdapter{
    private static final int HUMIDITY_DRAWABLE_ID = R.drawable.humidity;
    private HumidySensorData humidySensorData;

    public HumiditySensorAdapter(Context context, HumidySensorData humidySensorData) {
        super(context);
        this.humidySensorData = humidySensorData;
        if(this.humidySensorData == null){
            this.humidySensorData = new HumidySensorData();
        }
    }

    public void setHumidySensorData(HumidySensorData humidySensorData) {
        this.humidySensorData = humidySensorData;
        if(this.humidySensorData == null){
            this.humidySensorData = new HumidySensorData();
        }
    }


    @Override
    public Drawable getSensorImage() {

        return getContext().getResources().getDrawable(HUMIDITY_DRAWABLE_ID);
    }

    @Override
    public String getSensorData() {
        if(humidySensorData.getHumidityData() == null)
            return "";
        else
            return humidySensorData.getHumidityData().toString();
    }

    @Override
    public String getSensorDescription() {
        return getContext().getString(R.string.sensor_humidity);

    }
}
