package com.example.smarthomeapp.activities.xuhong;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.GpsInfo;
import com.example.smarthomeapp.model.SocialInfo;
import com.example.smarthomeapp.model.WearableDeviceInfo;
import com.example.smarthomeapp.result_jzp.EventQueryResolver;
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

import java.util.ArrayList;

public class ContrastElectricityActivity extends FragmentActivity {

    private LineChart contrastChart;
    private LineChart statisticChart;

    private Boolean flag;
    private final static int TIME = 20000;

    class StaticsHttpResultRecessListener<T> implements HttpResultProcessListener{
        Class<T> clas;

        public StaticsHttpResultRecessListener(Class<T> clas) {
            this.clas = clas;
        }

        @Override
        public void processing(int status, String responsString) {
            Gson gson= GsonUtil.create();
            if(clas.equals(SocialInfo.class)){
                 SocialInfo socialInfo = gson.fromJson(responsString,SocialInfo.class);
            }else if(clas.equals(GpsInfo.class)){
                GpsInfo gpsInfo = gson.fromJson(responsString,GpsInfo.class);
            }else if(clas.equals(WearableDeviceInfo.class)){
                WearableDeviceInfo wearableDeviceInfo = gson.fromJson(responsString,WearableDeviceInfo.class);
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
                    EventQueryResolver.queryGpsData(activity, new StaticsHttpResultRecessListener
                            <GpsInfo>(GpsInfo.class));
                    EventQueryResolver.queryWearableSensorData(activity, new StaticsHttpResultRecessListener
                            <WearableDeviceInfo>(WearableDeviceInfo.class));
                    EventQueryResolver.querySocialData(activity,new StaticsHttpResultRecessListener
                            <SocialInfo>(SocialInfo.class));


                }
            } catch (Exception e) {
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrast_electricity);
        contrastChart = (LineChart) findViewById(R.id.chart1);
        statisticChart = (LineChart) findViewById(R.id.chart2);

        drawChart(contrastChart, new float[]{1.5f, -0.5f, 31, 16});
        setData(24, 1f, contrastChart, "室内温度", "在家状态");

        drawChart(statisticChart, new float[]{2f, 0.5f, 15, 0});
        setData(24, 1f, statisticChart, "电价变化", "功率");
    }


    private void setData(int count, float range, LineChart lineChart,
                         String dataSet1, String dataSet2) {

        //draw x
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        //draw the first line
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry(range, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, dataSet1);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);


        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            yVals2.add(new Entry(range+26f, i));
        }

        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals2, dataSet2);
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(2f);
        set2.setCircleSize(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));

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
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
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