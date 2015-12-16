package com.example.smarthomeapp.model.che;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.smarthomeapp.R;


/**
 * Created by CHEYulin on 2015/5/17.
 */
public class Lamp extends Device{

    public Lamp(Context context) {
        super(context);
    }

    @Override
    public String getDeviceName() {
        return getContext().getString(R.string.lamp);
    }

    @Override
    public Drawable getDevicePicture() {
        return getContext().getResources().getDrawable(R.drawable.lamp);
    }

    @Override
    public void getDetailDialog(Object... objects) {

    }
}
