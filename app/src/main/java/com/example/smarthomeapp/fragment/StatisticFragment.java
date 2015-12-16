package com.example.smarthomeapp.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.activities.xuhong.UIMainActivity;
import com.example.smarthomeapp.dbhelper.DailyElectricityPriceHelper;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.DailyElectricityPrice;
import com.example.smarthomeapp.model.ElectricityInfo;
import com.example.smarthomeapp.model.PlrSensorData;
import com.example.smarthomeapp.model.TemperatureSensorData;
import com.example.smarthomeapp.result_jzp.StaticsDataQueryResolver;
import com.example.smarthomeapp.util.GsonUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticFragment extends Fragment {
    UIMainActivity uiMainActivity;
    private LineChart contrastChart;
    private LineChart statisticChart;

    private TemperatureSensorData[] temperatureSensorDatas;
    private PlrSensorData[] plrSensorDatas;
    private DailyElectricityPrice dailyElectricityPrice;
    private ElectricityInfo[] electricityInfos;
    private DailyElectricityPriceHelper dailyElectricityPriceHelper = new DailyElectricityPriceHelper();

    private Boolean flag;
    private final static int TIME = 5000;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm");


    class StaticsHttpResultRecessListener<T> implements HttpResultProcessListener {
        Class<T> clas;

        public StaticsHttpResultRecessListener(Class<T> clas) {
            this.clas = clas;
        }

        @Override
        public void processing(int status, String responsString) {
            Gson gson= GsonUtil.create();
            if((clas.equals(TemperatureSensorData[].class)) &&(status == 201)){
                 temperatureSensorDatas = gson.fromJson(responsString
                         ,TemperatureSensorData[].class);
            }else if((clas.equals(PlrSensorData[].class))&&(status == 201)){
                plrSensorDatas  = gson.fromJson(responsString
                        ,PlrSensorData[].class);
            }else if((clas.equals(DailyElectricityPrice.class))&&(status == 201)){
                dailyElectricityPrice  = gson.fromJson(responsString
                        ,DailyElectricityPrice.class);
            }else if((clas.equals(ElectricityInfo[].class))&&(status == 201)){
                electricityInfos  = gson.fromJson(responsString
                        ,ElectricityInfo[].class);
            }

        }

    }
    Activity activity;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                if (flag == true) {
                    handler.postDelayed(this, TIME);
                    StaticsDataQueryResolver.queryTemperatureArrayDatas(activity, new StaticsHttpResultRecessListener
                            <TemperatureSensorData[]>(TemperatureSensorData[].class));
                    StaticsDataQueryResolver.queryPlrArrayDatas(activity, new StaticsHttpResultRecessListener
                            <PlrSensorData[]>(PlrSensorData[].class));

                  StaticsDataQueryResolver.queryMeterArrayDatas(activity, new StaticsHttpResultRecessListener
                            <ElectricityInfo[]>(ElectricityInfo[].class));
                    StaticsDataQueryResolver.queryPriceDatas(activity, new StaticsHttpResultRecessListener
                            <DailyElectricityPrice>(DailyElectricityPrice.class));


                }
            } catch (Exception e) {
            }
        }
    };

    public StatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_contrast_electricity, container, false);
        contrastChart = (LineChart) view.findViewById(R.id.chart1);
        statisticChart = (LineChart) view.findViewById(R.id.chart2);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        flag = true;
        handler.postDelayed(runnable, 0);
        drawChart(contrastChart, new float[]{1.5f, -0.5f, 30, 0});
        if(temperatureSensorDatas != null && plrSensorDatas != null) {

            float[] temperatureDatas = new float[12];
            Date[] timexVals1 = new Date[12];
            String[] timeVals1 = new String[12];
            for (int i = 0; i < 48; i+=4) {
                temperatureDatas[i/4] = temperatureSensorDatas[i].getTemperatureData();
                Log.i("temperature:",i+""+temperatureDatas[i/4]);
//                timeVals1[i] = temperatureSensorDatas[i].getTemperatureDataCollectTime().toString();

                timeVals1[i/4] = ""+i/2+":00";


                try {
                    timexVals1[i/4] = format.parse(timeVals1[i/4]);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            float[] plrDatas = new float[12];
            for(int i=0;i<48;i=i+4){
                if(plrSensorDatas[i].getPlrData() == true){
                    plrDatas[i/4] = 1;
                }else
                    plrDatas[i/4] = 0;
            }

            setData(24, contrastChart, "在家状况", "室内温度",plrDatas,temperatureDatas,timeVals1);
        }



        drawChart(statisticChart, new float[]{1.3f, -0.5f, 20,0});
        if((dailyElectricityPrice != null)&&(electricityInfos != null)){
            dailyElectricityPriceHelper.setDailyElectricityPrice(dailyElectricityPrice);
            float[] totalEnergy = new float[electricityInfos.length];
            String[] timeVals2 = new String[electricityInfos.length];
            Date[] timexVals = new Date[electricityInfos.length];

            for(int i=0;i<electricityInfos.length;i++){
                totalEnergy[i]=electricityInfos[i].getActivePower()/100;
                    timeVals2[i] = ""+i+":00";
                try {
                    timexVals[i] = format.parse(timeVals2[i]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            setData(24, statisticChart, "电价变化", "功率",dailyElectricityPriceHelper.getDailyElectricityPrice(),totalEnergy,timeVals2);
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        flag = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        uiMainActivity = ((UIMainActivity) activity);
    }


    private void setData(int count, LineChart lineChart, String dataSet1
            , String dataSet2,float[] vals1,float[] vals2,String[] timevals) {

        //draw x
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; (i < count) && (i<vals1.length) && (i<vals2.length) ; i++) {
            xVals.add((timevals[i]) + "");
        }

        //draw the first line
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; (i < count) && (i<vals1.length) && (i<vals2.length) ; i++) {
            yVals1.add(new Entry(vals1[i], i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, dataSet1);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; (i < count) && (i<vals1.length) && (i<vals2.length) ; i++) {
            yVals2.add(new Entry(vals2[i], i));
        }

        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals2, dataSet2);
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setLineWidth(2f);
        set2.setCircleSize(3f);
        set2.setFillAlpha(65);//
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
//        set2.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set2);
        dataSets.add(set1); // add the datasets

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        lineChart.setData(data);
    }

    private void drawChart(LineChart lineChart, float[] values) {
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable value highlighting
        lineChart.setHighlightEnabled(true);
        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(true);
        lineChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);
        // set an alternative background color
        lineChart.setBackgroundColor(Color.LTGRAY);

        lineChart.animateX(2500);
        Typeface tf = Typeface.createFromAsset(this.getActivity().getAssets(), "OpenSans-Regular.ttf");

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        /*l.setTypeface(tf);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);*/
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(values[0]);
        leftAxis.setAxisMinValue(values[1]);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);


        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTypeface(tf);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(values[2]);
        rightAxis.setStartAtZero(false);
        rightAxis.setAxisMinValue(values[3]);
        rightAxis.setDrawGridLines(true);

    }




}
