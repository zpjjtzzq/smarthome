package com.example.smarthomeapp.model;

import java.security.Timestamp;

/**
 * Created by 卧龙风 on 2015/8/3.
 */
public class HumidySensorData implements java.io.Serializable {
    private Integer humidityDataId;
    private HumiditySensor humiditySensor;
    private Float humidityData;
    private Timestamp humidityDataCollectTime;

    public HumidySensorData() {

    }

    public HumidySensorData(HumiditySensor humiditySensor, Timestamp humidityDataCollectTime) {
        this.humiditySensor = humiditySensor;
        this.humidityDataCollectTime = humidityDataCollectTime;
    }

    public HumidySensorData(HumiditySensor humiditySensor, Float humidityData, Timestamp humidityDataCollectTime) {
        this.humiditySensor = humiditySensor;
        this.humidityData = humidityData;
        this.humidityDataCollectTime = humidityDataCollectTime;
    }

    public Integer getHumidityDataId() {
        return humidityDataId;
    }

    public void setHumiditySensor(HumiditySensor humiditySensor) {
        this.humiditySensor = humiditySensor;
    }

    public void setHumidityData(Float humidityData) {
        this.humidityData = humidityData;
    }

    public void setHumidityDataCollectTime(Timestamp humidityDataCollectTime) {
        this.humidityDataCollectTime = humidityDataCollectTime;
    }

    public HumiditySensor getHumiditySensor() {
        return humiditySensor;
    }

    public Float getHumidityData() {
        return humidityData;
    }

    public Timestamp getHumidityDataCollectTime() {
        return humidityDataCollectTime;
    }

    public void setHumidityDataId(Integer humidityDataId) {
        this.humidityDataId = humidityDataId;
    }
}
