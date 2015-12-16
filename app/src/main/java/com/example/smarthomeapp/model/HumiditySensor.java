package com.example.smarthomeapp.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 卧龙风 on 2015/8/3.
 */
public  class HumiditySensor implements java.io.Serializable  {
    private Integer humiditySensorId;
    private Box box;
    private Set<HumidySensorData> humidySensorDatas = new HashSet<HumidySensorData>(0);

    public HumiditySensor(Box box, Set<HumidySensorData> humidySensorDatas) {
        this.box = box;
        this.humidySensorDatas = humidySensorDatas;
    }

    public HumiditySensor() {
    }

    public Integer getHumiditySensorId() {
        return humiditySensorId;
    }

    public Box getBox() {
        return box;
    }

    public void setHumiditySensorId(Integer humiditySensorId) {
        this.humiditySensorId = humiditySensorId;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}

