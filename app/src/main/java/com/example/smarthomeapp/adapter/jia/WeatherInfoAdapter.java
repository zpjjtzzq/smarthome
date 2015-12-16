package com.example.smarthomeapp.adapter.jia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.model.weather.HeFengWeatherService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by 卧龙风 on 2015/8/4.
 */
public class WeatherInfoAdapter extends BaseAdapter {
    private Context context;
    private HeFengWeatherService heFengWeatherService;
    private WeatherInfoAdapter weatherInfoAdapter = this;

    public WeatherInfoAdapter(Context context) {
        this.context = context;
    }


    public void setHeFengWeatherService(HeFengWeatherService heFengWeatherService) {
        this.heFengWeatherService = heFengWeatherService;
    }



    private class ViewHolder{
        TextView dailyDate;
        TextView weekDate;
        TextView daily;
        TextView weather;
        TextView outdoorTemperature;
        TextView windSpeed;

        ImageView imageDayDescription;
        ImageView imageWind;


    }

    @Override
    public int getCount() {
        if(heFengWeatherService == null)
            return 0;
        else
            return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.outdoorenvironment_item, null);
            viewHolder.dailyDate = (TextView)convertView.findViewById(R.id.daily_date);
            viewHolder.weekDate = (TextView)convertView.findViewById(R.id.week_date);
            viewHolder.daily = (TextView)convertView.findViewById(R.id.day_or_night);
            viewHolder.imageDayDescription = (ImageView)convertView.findViewById(R.id.image_day_night);
            viewHolder.weather = (TextView)convertView.findViewById(R.id.weather);
            viewHolder.outdoorTemperature = (TextView)convertView.findViewById(R.id.outdoor_temperature);
            viewHolder.imageWind = (ImageView)convertView.findViewById(R.id.image_wind);
            viewHolder.windSpeed = (TextView)convertView.findViewById(R.id.text_wind);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Date date = new Date();
        int day = date.getDate();
        int week = date.getDay();
        String weekDescription ;
        weekDescription = weekSwitch(week);
        viewHolder.weekDate.setText("星期" + weekDescription);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String beginTime = heFengWeatherService.getHeWeatherDataService30s().get(0).getDailyForecasts()
                .get(0).getAstro().getSr();
        String endTime = heFengWeatherService.getHeWeatherDataService30s().get(0).getDailyForecasts()
                .get(0).getAstro().getSs();
        Calendar cal = Calendar.getInstance();
        String currentTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);


        try {
            Date beginDate = format.parse(beginTime + ":00");
            Date endData = format.parse(endTime + ":00");
            Date nowTime = format.parse(currentTime);

            viewHolder.dailyDate.setText("" + day+"日  "+currentTime);
            if(nowTime.after(beginDate) && nowTime.before(endData)){
                viewHolder.imageDayDescription.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.sun_day));
                viewHolder.daily.setText("白天");
            }
            else{
                viewHolder.imageDayDescription.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.moon_night));
                viewHolder.daily.setText("夜晚");
            }
        }catch (ParseException e ){
            e.printStackTrace();
        }

        String txtCond = heFengWeatherService.getHeWeatherDataService30s().get(0).
                getDailyForecasts().get(0).getCond().getTxtN();
        String txtDCond = heFengWeatherService.getHeWeatherDataService30s().get(0).
                getDailyForecasts().get(0).getCond().getTxtD();
        if(txtCond == null)
            if(txtDCond == null)
                viewHolder.weather.setText("多云");
             else
                viewHolder.weather.setText(""+txtDCond);
        else
            viewHolder.weather.setText(""+txtCond);

        int tmpMax = heFengWeatherService.getHeWeatherDataService30s().get(0).getDailyForecasts()
                .get(0).getTmp().getMax();
        int tmpMin = heFengWeatherService.getHeWeatherDataService30s().get(0).getDailyForecasts()
                .get(0).getTmp().getMin();
        viewHolder.outdoorTemperature.setText(tmpMin + "/" + tmpMax + "℃");
        viewHolder.imageWind.setImageDrawable(context.getResources().
                getDrawable(R.drawable.wind));
        String windStr = heFengWeatherService.getHeWeatherDataService30s().get(0).
                getHourlyForecasts().get(0).getWind().getSc();
        if(windStr != null)
            viewHolder.windSpeed.setText("" + windStr);
        else
            viewHolder.windSpeed.setText("中风");
        return convertView;
    }

    private String weekSwitch(int week){
        String weekDescription = "" ;
        switch (week){
            case 1:
                weekDescription = "一";
                break;
            case 2:
                weekDescription = "二";
                break;
            case 3:
                weekDescription = "三";
                break;
            case 4:
                weekDescription = "四";
                break;
            case 5:
                weekDescription = "五";
                break;
            case 6:
                weekDescription = "六";
                break;
            default:
                weekDescription = "日";
        }
        return weekDescription;
    }
}
