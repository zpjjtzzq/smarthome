package com.example.smarthomeapp.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.smarthomeapp.R;
import com.example.smarthomeapp.activities.AddRoomActivity;
import com.example.smarthomeapp.activities.jiazhanpei.ShowFamilylineInfo;
import com.example.smarthomeapp.activities.xuhong.ApplianceActivity;
import com.example.smarthomeapp.activities.xuhong.UIMainActivity;
import com.example.smarthomeapp.adapter.jia.WeatherInfoAdapter;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.HumidySensorData;
import com.example.smarthomeapp.model.LightSensorData;
import com.example.smarthomeapp.model.jia.HumiditySensorAdapter;
import com.example.smarthomeapp.model.weather.HeFengWeatherService;
import com.example.smarthomeapp.model.weather.HeWeatherDataService30;
import com.example.smarthomeapp.result_jzp.SensorQueryResolver;
import com.example.smarthomeapp.result_jzp.WeatherQueryResolver;
import com.example.smarthomeapp.util.GsonUtil;
import com.example.smarthomeapp.views.NoScorllGridView;
import com.google.gson.Gson;
import com.example.smarthomeapp.adapter.jia.SensorGridviewAdapter;
import com.example.smarthomeapp.model.FlameSensorData;
import com.example.smarthomeapp.model.GasSensorData;
import com.example.smarthomeapp.model.PlrSensorData;
import com.example.smarthomeapp.model.TemperatureSensorData;
import com.example.smarthomeapp.model.jia.FlameSensorAdapter;
import com.example.smarthomeapp.model.jia.GasSensorAdapter;
import com.example.smarthomeapp.model.jia.LightSensorAdapter;
import com.example.smarthomeapp.model.jia.PlrSensorAdapter;
import com.example.smarthomeapp.model.jia.SensorDataAdapter;
import com.example.smarthomeapp.model.jia.TemperatureSensorAdapter;

import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {
    private static final int TIME = 5000;
    private boolean flag;
    UIMainActivity uiMainActivity;
    View view;
    NoScorllGridView inDoorGridVIew;
    NoScorllGridView outDoorGridView;
    BootstrapButton familylineToBootStrapButton;
    BootstrapButton devicecontrolToBootStrapButton;
    Spinner spinner;
    private Button bnt_add_room;

    private SensorGridviewAdapter sensorGridviewAdapter;
    private WeatherInfoAdapter weatherInfoAdapter ;


     class WeatherResultProcessListener implements HttpResultProcessListener{
         @Override
         public void processing(int status, String responsString) {
             Gson gson = GsonUtil.create();
             HeFengWeatherService heWeatherDataServices = gson.fromJson(responsString
                    ,HeFengWeatherService.class);
             weatherInfoAdapter.setHeFengWeatherService(heWeatherDataServices);
             weatherInfoAdapter.notifyDataSetChanged();
         }
     }
    class MyHttpResultProcessListener<T> implements HttpResultProcessListener {
        Class<T> clas;

        /*HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {


        @Override
        public void processing(int status, String responsString) {
            Gson gson= GsonUtil.create();

            SensorDataAdapter[] sensorDataAdapters=sensorGridviewAdapter.getSensorDataAdapters();
            if(clas.equals(LightSensorData[].class)){
                ((LightSensorAdapter)sensorDataAdapters[1]).setLightSensorData(((LightSensorData[])gson.fromJson(responsString, clas))[0]);
            }else if(clas.equals(FlameSensorData[].class)){
                ((FlameSensorAdapter)sensorDataAdapters[4]).setFlameSensorData(((FlameSensorData[]) gson.fromJson(responsString, clas))[0]);
            }else if(clas.equals(PlrSensorData[].class)){
                ((PlrSensorAdapter)sensorDataAdapters[2]).setPlrSensorData(((PlrSensorData[]) gson.fromJson(responsString, clas))[0]);
            }else if(clas.equals(TemperatureSensorData[].class)){
                ((TemperatureSensorAdapter)sensorDataAdapters[0]).setTemperatureSensorData(((TemperatureSensorData[])  gson.fromJson(responsString, clas))[0]);
            }else if(clas.equals(GasSensorData[].class)){
                ((GasSensorAdapter)sensorDataAdapters[3]).setGasSensorData(((GasSensorData[]) gson.fromJson(responsString, clas))[0]);
            }else if(clas.equals(HumidySensorData[].class)){
                ((HumiditySensorAdapter)sensorDataAdapters[5]).setHumidySensorData(((HumidySensorData[])gson.fromJson(responsString,clas))[0]);
            }

            sensorGridviewAdapter.notifyDataSetChanged();
        }
    };

    Activity activity;
    HashSet<Integer> ids;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (flag) {
                    handler.postDelayed(this, TIME);
                    ids = new HashSet<Integer>();
                    ids.add(1);
                    SensorQueryResolver.querylightSensorData(activity, ids,
                            new MyHttpResultProcessListener<LightSensorData[]>(LightSensorData[].class));
                    SensorQueryResolver.queryflameSensorData(activity, ids,
                            new MyHttpResultProcessListener<FlameSensorData[]>(FlameSensorData[].class));
                    SensorQueryResolver.querygasSensorData(activity, ids,
                            new MyHttpResultProcessListener<GasSensorData[]>(GasSensorData[].class));
                    SensorQueryResolver.queryplrSensorData(activity, ids,
                            new MyHttpResultProcessListener<PlrSensorData[]>(PlrSensorData[].class));
                    SensorQueryResolver.querytemperatureSensorData(activity, ids,
                            new MyHttpResultProcessListener<TemperatureSensorData[]>(TemperatureSensorData[].class));
                    SensorQueryResolver.queryhumiditySensorData(activity, ids,
                            new MyHttpResultProcessListener<HumidySensorData[]>(HumidySensorData[].class));
                    SensorQueryResolver.querylightSensorData(activity, ids, new MyHttpResultProcessListener<LightSensorData[]>(LightSensorData[].class));
                    SensorQueryResolver.queryflameSensorData(activity, ids, new MyHttpResultProcessListener<FlameSensorData[]>(FlameSensorData[].class));
                    SensorQueryResolver.querygasSensorData(activity, ids, new MyHttpResultProcessListener<GasSensorData[]>(GasSensorData[].class));
                    SensorQueryResolver.queryplrSensorData(activity, ids, new MyHttpResultProcessListener<PlrSensorData[]>(PlrSensorData[].class));
                    SensorQueryResolver.querytemperatureSensorData(activity, ids, new MyHttpResultProcessListener<TemperatureSensorData[]>(TemperatureSensorData[].class));
                    SensorQueryResolver.queryhumiditySensorData(activity, ids, new MyHttpResultProcessListener<HumidySensorData[]>(HumidySensorData[].class));

                    WeatherQueryResolver.queryWeatherData(activity,new WeatherResultProcessListener());
                }
            } catch (Exception e) {
            }
        }
    };*/



        public MyHttpResultProcessListener(Class<T> clas) {
            this.clas = clas;
        }

        @Override
        public void processing(int status, String responsString) {
            Gson gson= GsonUtil.create();
            if(status == 201) {
                SensorDataAdapter[] sensorDataAdapters = sensorGridviewAdapter.getSensorDataAdapters();
                if (clas.equals(LightSensorData[].class)) {
                    ((LightSensorAdapter) sensorDataAdapters[1]).setLightSensorData(((LightSensorData[]) gson.fromJson(responsString, clas))[0]);
                } else if (clas.equals(FlameSensorData[].class)) {
                    ((FlameSensorAdapter) sensorDataAdapters[4]).setFlameSensorData(((FlameSensorData[]) gson.fromJson(responsString, clas))[0]);
                } else if (clas.equals(PlrSensorData[].class)) {
                    ((PlrSensorAdapter) sensorDataAdapters[2]).setPlrSensorData(((PlrSensorData[]) gson.fromJson(responsString, clas))[0]);
                } else if (clas.equals(TemperatureSensorData[].class)) {
                    ((TemperatureSensorAdapter) sensorDataAdapters[0]).setTemperatureSensorData(((TemperatureSensorData[]) gson.fromJson(responsString, clas))[0]);
                } else if (clas.equals(GasSensorData[].class)) {
                    ((GasSensorAdapter) sensorDataAdapters[3]).setGasSensorData(((GasSensorData[]) gson.fromJson(responsString, clas))[0]);
                } else if (clas.equals(HumidySensorData[].class)) {
                    ((HumiditySensorAdapter) sensorDataAdapters[5]).setHumidySensorData(((HumidySensorData[]) gson.fromJson(responsString, clas))[0]);
                }
            }

            sensorGridviewAdapter.notifyDataSetChanged();
        }
    }
    Activity activity;
    HashSet<Integer> ids;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (flag) {
                    handler.postDelayed(this, TIME);
                    ids = new HashSet<Integer>();
                    ids.add(1);
                    SensorQueryResolver.querylightSensorData(activity, ids,
                            new MyHttpResultProcessListener<LightSensorData[]>(LightSensorData[].class));
                    SensorQueryResolver.queryflameSensorData(activity, ids,
                            new MyHttpResultProcessListener<FlameSensorData[]>(FlameSensorData[].class));
                    SensorQueryResolver.querygasSensorData(activity, ids,
                            new MyHttpResultProcessListener<GasSensorData[]>(GasSensorData[].class));
                    SensorQueryResolver.queryplrSensorData(activity, ids,
                            new MyHttpResultProcessListener<PlrSensorData[]>(PlrSensorData[].class));
                    SensorQueryResolver.querytemperatureSensorData(activity, ids,
                            new MyHttpResultProcessListener<TemperatureSensorData[]>(TemperatureSensorData[].class));
                    SensorQueryResolver.queryhumiditySensorData(activity, ids,
                            new MyHttpResultProcessListener<HumidySensorData[]>(HumidySensorData[].class));
                }
            } catch (Exception e) {
            }
        }
    };

    Activity myActivity;
    Handler handlerWeather = new Handler();
    Runnable runnableWeather = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (flag) {
                    handlerWeather.postDelayed(this, 1800000);
                    WeatherQueryResolver.queryWeatherData(myActivity,new WeatherResultProcessListener());
                }
            } catch (Exception e) {
            }
        }
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("life_cycle","familyFragment onCreateView");
        view = inflater.inflate(R.layout.fragment_family, container, false);
        inDoorGridVIew = (NoScorllGridView) view.findViewById(R.id.grid_view_home_environment);
        outDoorGridView = (NoScorllGridView) view.findViewById(R.id.grid_view_outdoor_environment);
        familylineToBootStrapButton = (BootstrapButton)view.findViewById(R.id.familyline_to_bootstrapbutton);
        devicecontrolToBootStrapButton = (BootstrapButton)view.findViewById(R.id.devicecontrol_to_bootstrapbutton);

        //spinner
        spinner = (Spinner)view.findViewById(R.id.spinner_select_room);
        String[] strings = new String[3];
        for(int i = 0; i < strings.length; i++){
            strings[i] = "房间"+ String.valueOf(i);
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, strings);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(uiMainActivity, "切换到房间" + String.valueOf(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /////////////////////////////////////////////////////

        //add room
        bnt_add_room = (Button)view.findViewById(R.id.add_room);
        bnt_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(uiMainActivity, AddRoomActivity.class);
                intent.putExtra("user_id", uiMainActivity.user_id);
                startActivity(intent);
            }
        });
        //////////////////////////////////////////////////////////////////////

        familylineToBootStrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ShowFamilylineInfo.class);
                startActivity(intent);
            }
        });

        devicecontrolToBootStrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ApplianceActivity.class);
                startActivity(intent);
            }
        });

        sensorGridviewAdapter = new SensorGridviewAdapter(this.getActivity(), null);
        sensorGridviewAdapter.setSensorDataAdapters(new SensorDataAdapter[6]);
                inDoorGridVIew.setAdapter(sensorGridviewAdapter);
        sensorGridviewAdapter.getSensorDataAdapters()[0]=new TemperatureSensorAdapter(this.getActivity(),null);
        sensorGridviewAdapter.getSensorDataAdapters()[1]=new LightSensorAdapter(this.getActivity(),null);
        sensorGridviewAdapter.getSensorDataAdapters()[2]=new PlrSensorAdapter(this.getActivity(),null);
        sensorGridviewAdapter.getSensorDataAdapters()[5]=new HumiditySensorAdapter(this.getActivity(),null);
        sensorGridviewAdapter.getSensorDataAdapters()[4]=new FlameSensorAdapter(this.getActivity(),null);
        sensorGridviewAdapter.getSensorDataAdapters()[3]=new GasSensorAdapter(this.getActivity(),null);


        weatherInfoAdapter = new WeatherInfoAdapter(this.getActivity());
        outDoorGridView.setAdapter(weatherInfoAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("life_cycle","familyFragment onAttach");
        uiMainActivity = ((UIMainActivity) activity);
        Log.d("family_fragment", uiMainActivity.user_id.toString());
        //根据user_id查询房间数量, 构建房间列表。
    }

    @Override
    public void onResume() {
        Log.i("life_cycle","familyFragment onResume");
        super.onResume();
        flag = true;
        handler.postDelayed(runnable, 0);
        //handlerWeather.postDelayed(runnableWeather, 0);
        sensorGridviewAdapter.notifyDataSetChanged();
     /*   weatherInfoAdapter = new WeatherInfoAdapter(this.getActivity());
        outDoorGridView.setAdapter(weatherInfoAdapter);*/
    }

    @Override
    public void onPause() {
        Log.i("life_cycle","FamilyFragment onPause");
        super.onPause();
        flag = false;
    }
}

