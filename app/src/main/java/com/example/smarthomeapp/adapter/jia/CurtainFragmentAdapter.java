package com.example.smarthomeapp.adapter.jia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smarthomeapp.R;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.CurtainStatus;
import com.example.smarthomeapp.result_jzp.AppliancesUpdateResolver;

/**
 * Created by 卧龙风 on 2015/8/4.
 */
public class CurtainFragmentAdapter extends BaseAdapter {
    private Context context;
    private CurtainStatus[] curtainStatuses;
    private CurtainFragmentAdapter curtainFragmentAdapter = this;
    private boolean[] isON;

    public CurtainFragmentAdapter(Context context, CurtainStatus[] curtainStatuses, boolean[] isON) {
        this.context = context;
        this.curtainStatuses = curtainStatuses;
        this.isON = isON;
    }

    public void setCurtainStatuses(CurtainStatus[] curtainStatuses) {
        this.curtainStatuses = curtainStatuses;
    }

    private HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {
        @Override
        public void processing(int status, String responsString) {

            if (status == 201) {
                Toast.makeText(context, "窗帘控制成功", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (curtainStatuses == null)
            return 0;
        else
            return curtainStatuses.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
//        ImageView curtainDescription;
        ImageView curtainSlideUp;
        ImageView curtainSlideStop;
        ImageView curtainSlideDown;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_curtain, null);
            holder = new ViewHolder();

//            holder.curtainDescription = (ImageView)convertView.findViewById(R.id.curtain_description);
            holder.curtainSlideUp = (ImageView)convertView.findViewById(R.id.curtain_slideup);
            holder.curtainSlideStop = (ImageView)convertView.findViewById(R.id.curtain_slidestop);
            holder.curtainSlideDown = (ImageView)convertView.findViewById(R.id.curtain_slidedown);

            holder.curtainSlideUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curtainStatuses[position].setCurtainStatusId(1);
                    curtainStatuses[position].setCurtainStatus((float) 1);

                    AppliancesUpdateResolver.updateCurtainStatus(context,curtainStatuses[position],httpResultProcessListener);
                    curtainFragmentAdapter.notifyDataSetChanged();
                }
            });
            holder.curtainSlideStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curtainStatuses[position].setCurtainStatusId(1);
                    curtainStatuses[position].setCurtainStatus((float) 2);

                    AppliancesUpdateResolver.updateCurtainStatus(context,curtainStatuses[position],httpResultProcessListener);
                    curtainFragmentAdapter.notifyDataSetChanged();
                }
            });
            holder.curtainSlideDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curtainStatuses[position].setCurtainStatusId(1);
                    curtainStatuses[position].setCurtainStatus((float)3);

                    AppliancesUpdateResolver.updateCurtainStatus(context,curtainStatuses[position],httpResultProcessListener);
                    curtainFragmentAdapter.notifyDataSetChanged();
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.curtainSlideUp.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slideup_off));
        holder.curtainSlideStop.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_stop_on));
        holder.curtainSlideDown.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slidedown_off));
//        holder.curtainDescription.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_1));

       if(curtainStatuses[position].getCurtainStatus() == (float)1){
           holder.curtainSlideUp.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slideup_on));
           holder.curtainSlideStop.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_stop_off));
           holder.curtainSlideDown.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slidedown_off));
//           holder.curtainDescription.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_1));
       }
        if(curtainStatuses[position].getCurtainStatus() == (float)2) {
            holder.curtainSlideUp.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slideup_off));
            holder.curtainSlideStop.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_stop_on));
            holder.curtainSlideDown.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slidedown_off));
//            holder.curtainDescription.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_1));

        }
        if(curtainStatuses[position].getCurtainStatus() == (float)3){
            holder.curtainSlideUp.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slideup_off));
            holder.curtainSlideStop.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_stop_off));
            holder.curtainSlideDown.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_slidedown_on));
//            holder.curtainDescription.setImageDrawable(context.getResources().getDrawable(R.drawable.curtain_1));
        }
        return convertView;
    }
}

