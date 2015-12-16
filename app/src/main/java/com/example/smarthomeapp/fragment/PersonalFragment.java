package com.example.smarthomeapp.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.activities.xuhong.UIMainActivity;
import com.example.smarthomeapp.adapter.EventExpandableAdapter;
import com.example.smarthomeapp.adapter.ShemsTxtImgTxtAdapter;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.DecisionInfo;
import com.example.smarthomeapp.model.FlameSensorData;
import com.example.smarthomeapp.model.GasSensorData;
import com.example.smarthomeapp.model.GpsInfo;
import com.example.smarthomeapp.model.HumidySensorData;
import com.example.smarthomeapp.model.LightSensorData;
import com.example.smarthomeapp.model.PlrSensorData;
import com.example.smarthomeapp.model.SocialInfo;
import com.example.smarthomeapp.model.TemperatureSensorData;
import com.example.smarthomeapp.model.WearableDeviceInfo;
import com.example.smarthomeapp.model.che.AirConditioner;
import com.example.smarthomeapp.model.che.Curtain;
import com.example.smarthomeapp.model.che.Device;
import com.example.smarthomeapp.model.che.DrinkerMachine;
import com.example.smarthomeapp.model.che.EventDetection;
import com.example.smarthomeapp.model.che.Lamp;
import com.example.smarthomeapp.model.che.WaterHeater;
import com.example.smarthomeapp.model.jia.FlameSensorAdapter;
import com.example.smarthomeapp.model.jia.GasSensorAdapter;
import com.example.smarthomeapp.model.jia.HumiditySensorAdapter;
import com.example.smarthomeapp.model.jia.LightSensorAdapter;
import com.example.smarthomeapp.model.jia.PlrSensorAdapter;
import com.example.smarthomeapp.model.jia.SensorDataAdapter;
import com.example.smarthomeapp.model.jia.TemperatureSensorAdapter;
import com.example.smarthomeapp.model.weather.HeFengWeatherService;
import com.example.smarthomeapp.result_jzp.DecisionQueryResolver;
import com.example.smarthomeapp.result_jzp.EventQueryResolver;
import com.example.smarthomeapp.result_jzp.SensorQueryResolver;
import com.example.smarthomeapp.result_jzp.WeatherQueryResolver;
import com.example.smarthomeapp.services.CollectDataService;
import com.example.smarthomeapp.util.GsonUtil;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    UIMainActivity uiMainActivity;
    private ExpandableListView expandableListView;
    private EventExpandableAdapter eventExpandableAdapter;
    private List<EventDetection> eventDetectionList = new LinkedList<EventDetection>();;
    private Spinner spinner;
    private View view;
    private ListView socialListView;
    private ListView movingStatusListView;
    private ShemsTxtImgTxtAdapter socialAdapter;
    private ShemsTxtImgTxtAdapter movingStatusAdapter;
    private Boolean flag;
    private final static int TIME = 5000;
    private SocialInfo socialInfo;
    private WearableDeviceInfo wearableDeviceInfo;
    private GpsInfo gpsInfo;
    private DecisionInfo[] decisionInfos;
    private Context this_activity = this.getActivity();
    private String event_start_time = "";

    public PersonalFragment() {
        // Required empty public constructor
    }

    class PersonalHttpResultProcessListener<T> implements HttpResultProcessListener {
        Class<T> clas;

        public PersonalHttpResultProcessListener(Class<T> clas) {
            this.clas = clas;
        }

        @Override
        public void processing(int status, String responsString) {
            Gson gson= GsonUtil.create();
            if((clas.equals(SocialInfo.class)) && (status == 201)){
                socialInfo = gson.fromJson(responsString,SocialInfo.class);
            }else if((clas.equals(GpsInfo.class))&& (status == 201)){
                gpsInfo = gson.fromJson(responsString,GpsInfo.class);
            }else if((clas.equals(WearableDeviceInfo.class))&& (status == 201)){
                wearableDeviceInfo = CollectDataService.wearableDeviceInfo;
                 //wearableDeviceInfo = gson.fromJson(responsString,WearableDeviceInfo.class);
            }
            if(clas.equals(GpsInfo.class) || clas.equals(WearableDeviceInfo.class)){
                if((gpsInfo != null) && (wearableDeviceInfo!=null)) {
                    movingStatusAdapter.setValueStrLIst(new String[]{
                            wearableDeviceInfo.getHeartRate().toString(), wearableDeviceInfo.getSpeed().toString() + "m/s",
                            wearableDeviceInfo.getBodyTemperature().toString() + getString(R.string.temp_symbol), "学校"/*gpsInfo.getLocationType().getLocType()*/});
                    movingStatusAdapter.notifyDataSetChanged();
                }
                else {
                    movingStatusAdapter.setValueStrLIst(new String[]{"72", "20 m/s", "37" + getString(R.string.temp_symbol), "学校"});
                    movingStatusAdapter.notifyDataSetChanged();
                }
            }
            if(clas.equals(SocialInfo.class)) {
                if (socialInfo != null) {
                    String activityType = socialInfo.getActivityType().getActivityType();
                    int sourceTypeId = socialInfo.getSocialSource().getSourceTypeId();
                    switch (sourceTypeId) {
                        case 3:
                            socialAdapter.setValueStrLIst(new String[]{activityType, "暂无", "暂无"});
                            break;
                        case 2:
                            socialAdapter.setValueStrLIst(new String[]{"暂无", activityType, "暂无"});
                            break;
                        case 1:
                            socialAdapter.setValueStrLIst(new String[]{"暂无", "暂无", activityType});
                            break;
                    }
                } else
                    socialAdapter.setValueStrLIst(new String[]{"暂无", "暂无", "暂无"});
                socialAdapter.notifyDataSetChanged();
                if (socialInfo != null) {
                    String startTime = socialInfo.getStartTime();
                    if (!startTime.equals(event_start_time)) {
                        String[] hours = startTime.split(" |\\.");

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        try {
                            Date date = sdf.parse(hours[1]);
                            String[] eventSources = new String[]{getString(R.string.weibo)};
                            String eventDescription = null;
                            List<Device> deviceList = new ArrayList<Device>();
                            int acType = socialInfo.getActivityType().getActivityTypeId();
                            String eventName = null;
                            if (acType == 1) {
                                eventName = "运动";
                                eventDescription = "检测到用户运动活动";
                                deviceList.add(new AirConditioner(getActivity()));
                                deviceList.add(new DrinkerMachine(getActivity()));
                            } else if (acType == 2) {
                                eventName = "吃饭";
                                eventDescription = "检测到用户吃饭事件";
                                deviceList.add(new Lamp(getActivity()));
                                deviceList.add(new Curtain(getActivity()));
                            } else if (acType == 3) {
                                eventName = "延迟";
                                eventDescription = "检测到用户活动延迟";
                            } else if (acType == 4) {
                                eventName = "上班";
                                eventDescription = "检测到用户上班事件";
                                deviceList.add(new Lamp(getActivity()));
                                deviceList.add(new Curtain(getActivity()));
                            }else if (acType == 5){
                                eventName = "下班";
                                eventDescription = "检测到用户下班事件";
                                deviceList.add(new Lamp(getActivity()));
                                deviceList.add(new Curtain(getActivity()));
                            }

                            EventDetection eventDetection = new EventDetection(getActivity(), date, eventSources, eventName, eventDescription, deviceList);
                            eventDetectionList.add(0, eventDetection);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        eventExpandableAdapter.notifyDataSetChanged();
                        event_start_time = startTime;
                    }
                } else {
                    //do nothing
                }
            }
        };
    }
    Activity activity;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (flag) {
                    handler.postDelayed(this, TIME);
                    EventQueryResolver.queryGpsData(activity, new PersonalHttpResultProcessListener
                            <GpsInfo>(GpsInfo.class));
                    EventQueryResolver.queryWearableSensorData(activity, new PersonalHttpResultProcessListener
                            <WearableDeviceInfo>(WearableDeviceInfo.class));
                    EventQueryResolver.querySocialData(activity,new PersonalHttpResultProcessListener
                            <SocialInfo>(SocialInfo.class));

                }
            } catch (Exception e) {
                //do nothing
            }
        }
    };

    class DecisionInfoHttpResultProcessListener implements HttpResultProcessListener{
        @Override
        public void processing(int status, String responsString) {
            Gson gson = GsonUtil.create();
            decisionInfos = gson.fromJson(responsString,DecisionInfo[].class);
        }
    }
    Handler dicisionHandler = new Handler();
    Runnable dicisionRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                if(flag){
                    dicisionHandler.postDelayed(this,TIME);
                    DecisionQueryResolver.queryDecisionInfo(activity,new DecisionInfoHttpResultProcessListener());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private void findViews() {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_view_event_detection);
        spinner = (Spinner) view.findViewById(R.id.spinner_event_time);
        socialListView= (ListView) view.findViewById(R.id.listview_social_status);
        movingStatusListView= (ListView) view.findViewById(R.id.listview_moving_status);
    }

    private void initListViewData() {
        /*date = new Date();
        date.setHours(8);
        date.setMinutes(10);
        eventSources = new String[]{getString(R.string.gps)};
        eventDescription = getString(R.string.go_out_home);
        eventName = getString(R.string.simple_go_out_home);
        deviceList = new ArrayList<Device>();
        deviceList.add(new AirConditioner(this.getActivity()));
        deviceList.add(new DrinkerMachine(this.getActivity()));
        eventDetection = new EventDetection(this.getActivity(), date, eventSources, eventName, eventDescription, deviceList);
        eventDetectionList.add(0, eventDetection);

        date = new Date();
        date.setHours(8);
        date.setMinutes(22);
        eventSources = new String[]{getString(R.string.gps), getString(R.string.api_bai_du_map)};
        eventDescription = getString(R.string.traffic_jam);
        eventName = getString(R.string.simple_traffic_jam);
        deviceList = new ArrayList<Device>();
        deviceList.add(new AirConditioner(this.getActivity()));
        deviceList.add(new DrinkerMachine(this.getActivity()));
        eventDetection = new EventDetection(this.getActivity(), date, eventSources, eventName, eventDescription, deviceList);
        eventDetectionList.add(0, eventDetection);

        date = new Date();
        date.setHours(11);
        date.setMinutes(32);
        eventSources = new String[]{getString(R.string.weibo)};
        eventDescription = getString(R.string.social_plan_sports);
        eventName = getString(R.string.simple_plan_sports);
        deviceList = new ArrayList<Device>();
        deviceList.add(new WaterHeater(this.getActivity()));
        eventDetection = new EventDetection(this.getActivity(), date, eventSources, eventName, eventDescription, deviceList);
        eventDetectionList.add(0, eventDetection);

        date = new Date();
        date.setHours(14);
        date.setMinutes(47);
        eventSources = new String[]{getString(R.string.hand_ring)};
        eventDescription = getString(R.string.play_sports);
        eventName = getString(R.string.simple_play_sports);
        deviceList = new ArrayList<Device>();
        deviceList.add(new WaterHeater(this.getActivity()));
        deviceList.add(new AirConditioner(this.getActivity()));
        eventDetection = new EventDetection(this.getActivity(), date, eventSources, eventName, eventDescription, deviceList);
        eventDetectionList.add(0, eventDetection);*/
    }

    private void setAdapters() {
        eventExpandableAdapter = new EventExpandableAdapter(eventDetectionList, this.getActivity());
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(eventExpandableAdapter);
        String[] stringList = new String[]{"1h", "3h", "6h", "24h"};
        ArrayAdapter<String> myaAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, stringList);
        spinner.setAdapter(myaAdapter);
        String [] descriptionStrs=getActivity().getResources().getStringArray(R.array.social_status_array);
        Context context=getActivity();
        socialAdapter=new ShemsTxtImgTxtAdapter(this.getActivity(),descriptionStrs,
                new Drawable[]{context.getResources().getDrawable(R.drawable.weibo),context.getResources().getDrawable(R.drawable.duanxin),context.getResources().getDrawable(R.drawable.map)});

        descriptionStrs=getActivity().getResources().getStringArray(R.array.moving_status_array);
        movingStatusAdapter=new ShemsTxtImgTxtAdapter(this.getActivity(),descriptionStrs,
                new Drawable[]{context.getResources().getDrawable(R.drawable.heartrate),context.getResources().getDrawable(R.drawable.speed),
                        context.getResources().getDrawable(R.drawable.temperature),context.getResources().getDrawable(R.drawable.map)});
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        findViews();
        //initListViewData();
        setAdapters();
        socialListView.setAdapter(socialAdapter);
        socialListView.setDivider(null);
        movingStatusListView.setAdapter(movingStatusAdapter);
        movingStatusListView.setDivider(null);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        uiMainActivity = ((UIMainActivity) activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = true;
        handler.postDelayed(runnable, 0);
        /*if((gpsInfo != null) && (wearableDeviceInfo!=null))
            movingStatusAdapter.setValueStrLIst(new String[]{
                    wearableDeviceInfo.getHeartRate().toString(), wearableDeviceInfo.getSpeed().toString()+"m/s",
                    wearableDeviceInfo.getBodyTemperature().toString()+getString(R.string.temp_symbol), gpsInfo.getLocationType().getLocType()});
        else
            movingStatusAdapter.setValueStrLIst(new String[]{"72", "20 m/s", "37"+getString(R.string.temp_symbol), "学校"});*/

        /*if(socialInfo != null){
            String activityType = socialInfo.getActivityType().getActivityType();
            int sourceTypeId =socialInfo.getSocialSource().getSourceTypeId();
            switch (sourceTypeId){
                case 3:
                    socialAdapter.setValueStrLIst(new String[]{activityType,"暂无","暂无"});
                    break;
                case 2:
                    socialAdapter.setValueStrLIst(new String[]{"暂无",activityType,"暂无"});
                    break;
                case 1:
                    socialAdapter.setValueStrLIst(new String[]{"暂无","暂无",activityType});
                    break;

            }
        }
        else
            socialAdapter.setValueStrLIst(new String[]{"暂无","暂无","暂无"});*/
    }

    @Override
    public void onPause() {
        super.onPause();
        flag = false;
    }
}
