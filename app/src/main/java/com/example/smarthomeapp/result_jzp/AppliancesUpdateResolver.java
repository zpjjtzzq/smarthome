package com.example.smarthomeapp.result_jzp;

import android.content.Context;
import android.util.Log;

import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.AirConditionStatus;
import com.example.smarthomeapp.model.CurtainStatus;
import com.example.smarthomeapp.model.LampStatus;
import com.example.smarthomeapp.model.Room;
import com.example.smarthomeapp.model.SheSwitchStatus;
import com.example.smarthomeapp.model.UserInfo;
import com.example.smarthomeapp.model.WaterHeaterStatus;
import com.example.smarthomeapp.util.AsyncHttpUtil;
import com.example.smarthomeapp.util.GsonUtil;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by ������ on 2015/7/22.
 */
public class AppliancesUpdateResolver implements
        HttpResultProcessListener{

    public static void updateLampStatus(Context context,LampStatus lampStatus,
                                   HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/appliance/lamp";
        Gson gson = GsonUtil.create();
        lampStatus.setIsControlledByUser(false);
        lampStatus.setIsAlreadyControlled(false);
        String tmpstr = gson.toJson(lampStatus, LampStatus.class);
        Log.d("appliance_control", tmpstr);
        AsyncHttpUtil.requestJson(context, url, tmpstr, listener);
    }


    public static void updateCurtainStatus(Context context,CurtainStatus curtainStatus ,
                                         HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/appliance/curtain";
        curtainStatus.setCurtainStatusRecordTime(new Timestamp(System.currentTimeMillis()));
        curtainStatus.setIsControlledByUser(false);
        curtainStatus.setIsAlreadyControlled(true);

        Gson gson = GsonUtil.create();
        String tmpstr = gson.toJson(curtainStatus, CurtainStatus.class);
        AsyncHttpUtil.requestJson(context,url,tmpstr,listener);
    }

    public static void updateSheSwitchStatus(Context context,SheSwitchStatus sheSwitchStatus ,
                                            HttpResultProcessListener listener){

        String url = "http://202.117.14.247:8080/smarthome/appliance/sheSwitch";
        Gson gson = GsonUtil.create();
        sheSwitchStatus.setIsAlreadyControlled(false);
        String tmpstr = gson.toJson(sheSwitchStatus, SheSwitchStatus.class);
        AsyncHttpUtil.requestJson(context,url,tmpstr,listener);
    }

    public static void updateAirConditionStatus(Context context,AirConditionStatus airConditionStatus  ,
                                            HttpResultProcessListener listener){

        String url = "http://202.117.14.247:8080/smarthome/appliance/airCondition";
        Gson gson = GsonUtil.create();
        airConditionStatus.setIsAlreadyControlled(false);
        airConditionStatus.setIsControlledByUser(false);
        String tmpstr = gson.toJson(airConditionStatus, AirConditionStatus.class);
        AsyncHttpUtil.requestJson(context,url,tmpstr,listener);
    }

    public static void updateWaterHeaterStatus(Context context,WaterHeaterStatus waterHeaterStatus  ,
                                               HttpResultProcessListener listener){
        String url = "http://202.117.14.247:8080/smarthome/appliance/waterHeater";
        Gson gson = GsonUtil.create();
        waterHeaterStatus.setIsAlreadyControlled(false);
        String tmpstr = gson.toJson(waterHeaterStatus, WaterHeaterStatus.class);
        AsyncHttpUtil.requestJson(context,url,tmpstr,listener);
    }

    /*创建房间*/
    public static void create_room(Context context, Room room, HttpResultProcessListener listener){
        String url = "http://192.168.1.39:8080/smarthome/room/saveRoom";
        Gson gson = GsonUtil.create();
        String tmpstr = gson.toJson(room, Room.class);
        Log.d("add_room", tmpstr);
        AsyncHttpUtil.requestJson(context, url, tmpstr, listener);
    }
    /*创建用户*/
    public static void create_user(Context context, UserInfo userInfo, HttpResultProcessListener listener){
        String url = "http://192.168.1.39:8080/smarthome/userInfo/saveUserInfo";
        Gson gson = GsonUtil.create();
        String tmpstr = gson.toJson(userInfo, UserInfo.class);
        AsyncHttpUtil.requestJson(context, url, tmpstr, listener);
    }
    /*登陆*/
    public static void app_login(Context context, ArrayList<String> name_pwd, HttpResultProcessListener listener){
        String url = "http://192.168.1.39:8080/smarthome/userInfo/UserLogin";
        Gson gson = GsonUtil.create();
        String tmpstr = gson.toJson(name_pwd, ArrayList.class);
        AsyncHttpUtil.requestJson(context, url, tmpstr, listener);
    }
    /*查询房间*/
    public static void query_room(Context context, Integer user_id, HttpResultProcessListener listener){
        String url = "http://192.168.1.39:8080/smarthome/room/searchroom/" + user_id.toString();
        Log.d("login", url);
        Gson gson = GsonUtil.create();
        String tmpstr = gson.toJson(user_id, Integer.class);
        //AsyncHttpUtil.requestJson(context, url, tmpstr, listener);
        AsyncHttpUtil.requestJsonViaGet(context, url, listener);
    }

    public void processing(int status, String responsString){
        Log.d(AppliancesUpdateResolver.class.getSimpleName(), responsString);
    }

}

