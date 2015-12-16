package com.example.smarthomeapp.activities.xuhong;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.smarthomeapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class StatisticComplaindegreeActivity extends FragmentActivity {
    private BarChart statisticsBarChart;

    private void findViews() {
        this.statisticsBarChart = (BarChart) findViewById(R.id.chart_statistics);

    }

    private void initBartChart() {
        statisticsBarChart.setDescription("");
        // scaling can now only be done on x- and y-axis separately
        statisticsBarChart.setPinchZoom(false);
        statisticsBarChart.setDrawBarShadow(false);
        statisticsBarChart.setDrawGridBackground(true);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = statisticsBarChart.getLegend();
        l.setEnabled(true);
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setTypeface(tf);
//        l.setTextSize(12f);

        XAxis xl = statisticsBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setTextSize(12f);
        xl.setDrawGridLines(true);
        xl.setLabelsToSkip(0);
        statisticsBarChart.getAxisLeft().setEnabled(true);
        statisticsBarChart.setDescription("barchart");
        statisticsBarChart.getAxisRight().setEnabled(false);
    }

    private void bindData2BarChart() {
        ArrayList<String> xValList = new ArrayList<String>();
        ArrayList<BarEntry> yValFormerMonthList = new ArrayList<BarEntry>();

        for(int i = 0;i<24 ; i++){
            xValList.add(""+i);
            yValFormerMonthList.add(new BarEntry((float)(i+Math.random()*5),i));
        }
       /* int i = 0;
        yValFormerMonthList.add(new BarEntry((float)1.5, i));
        i++;
        yValFormerMonthList.add(new BarEntry((float)2, i));
        i++;
        yValFormerMonthList.add(new BarEntry((float)2.5, i));
*/
        BarDataSet setFormer = new BarDataSet(yValFormerMonthList, getString(R.string.txt_former_month));
        setFormer.setBarSpacePercent(40f);
        setFormer.setColors(new int[]{Color.rgb(255, 249, 133), Color.rgb(196, 255, 134), Color.rgb(136, 235, 255)});

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setFormer);

        BarData data = new BarData(xValList, dataSets);

        data.setValueTextSize(12f);
        statisticsBarChart.setData(data);

        data.setGroupSpace(80f);
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        data.setValueTypeface(tf);


        statisticsBarChart.setDescription("barchark");
        statisticsBarChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statistic);
        findViews();
        initBartChart();
        bindData2BarChart();
    }

}
