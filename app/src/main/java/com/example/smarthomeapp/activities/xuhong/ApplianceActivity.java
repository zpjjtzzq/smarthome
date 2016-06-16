package com.example.smarthomeapp.activities.xuhong;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.smarthomeapp.R;
import com.example.smarthomeapp.fragment.AirconditionerFrgament;
import com.example.smarthomeapp.fragment.CurtainFragment;
import com.example.smarthomeapp.fragment.HeaterFragment;
import com.example.smarthomeapp.fragment.LightFragment;
import com.example.smarthomeapp.fragment.PlugFragment;
import com.example.smarthomeapp.listener.HttpResultProcessListener;
import com.example.smarthomeapp.model.Room;
import com.example.smarthomeapp.result_jzp.AppliancesUpdateResolver;
import com.example.smarthomeapp.util.GsonUtil;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

public class ApplianceActivity extends FragmentActivity {
    private ActionBar actionBar;
    private TextView actionBarTextView;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private List<Fragment> fragmentsList;
    private SHEMSPagerTitleAdapter shemsPagerTitleAdapter;
    private Room[] rooms;
    private int user_id;
    private Integer room_id;
    private Integer pos;

    public void setActionBarText(String string){
        actionBarTextView.setText(string);
    }

    private void findViews() {
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 0);
        room_id = intent.getIntExtra("room_id", 0);
        actionBar = getActionBar();
        viewPager = (ViewPager) findViewById(R.id.viewpager_appliance_control);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab_appliance_control);
    }

    class SHEMSPagerTitleAdapter extends FragmentPagerAdapter {
        private ArrayList<String > TITLES = new ArrayList<>();

        public SHEMSPagerTitleAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setAdapt(){
            TITLES.clear();
            if(user_id != 0) {
                if (rooms[pos].getIsExistedAirCondition()) TITLES.add("空调");
                if (rooms[pos].getIsExistedWaterHeater()) TITLES.add("热水器");
                if (rooms[pos].getIsExistedLamp()) TITLES.add("电灯");
                if (rooms[pos].getIsExistedSheSwitch()) TITLES.add("插头");
                if (rooms[pos].getIsExistedCurtain()) TITLES.add("窗帘");
            }else{
                TITLES.add("空调");
                TITLES.add("热水器");
                TITLES.add("电灯");
                TITLES.add("插头");
                TITLES.add("窗帘");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return TITLES.size();
        }
    }

    private void initActionBar() {
        //�����Զ���
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.item_actionbar);
        actionBarTextView = (TextView) findViewById(R.id.txt_hint_title);
        ImageButton button = (ImageButton) findViewById(R.id.btn_back);
        button.setImageDrawable(getResources().getDrawable(R.drawable.icon_smarthome));
    }

    private void initViewPager(){
        fragmentsList = new ArrayList<>();
        if(user_id != 0) {
            if (rooms[pos].getIsExistedAirCondition())
                fragmentsList.add(new AirconditionerFrgament());
            if (rooms[pos].getIsExistedWaterHeater())
                fragmentsList.add(new HeaterFragment());
            if (rooms[pos].getIsExistedLamp())
                fragmentsList.add(new LightFragment());
            if (rooms[pos].getIsExistedSheSwitch())
                fragmentsList.add(new PlugFragment());
            if (rooms[pos].getIsExistedCurtain())
                fragmentsList.add(new CurtainFragment());
        }else{
            fragmentsList.add(new AirconditionerFrgament());
            fragmentsList.add(new HeaterFragment());
            fragmentsList.add(new LightFragment());
            fragmentsList.add(new PlugFragment());
            fragmentsList.add(new CurtainFragment());
        }
        shemsPagerTitleAdapter = new SHEMSPagerTitleAdapter(getSupportFragmentManager());
        shemsPagerTitleAdapter.setAdapt();
        viewPager.setAdapter(shemsPagerTitleAdapter);
        shemsPagerTitleAdapter.notifyDataSetChanged();

        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        setActionBarText(getString(R.string.applicant_airconditioner));
                        break;
                    case 1:
                        setActionBarText(getString(R.string.appliccant_heater));
                        break;
                    case 2:
                        setActionBarText(getString(R.string.applicant_light));
                        break;
                    case 3:
                        setActionBarText(getString(R.string.applicant_plug));
                        break;
                    case 4:
                        setActionBarText(getString(R.string.applicant_curtain));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.actionbar));

        viewPager.setCurrentItem(0);
   }

    private void  initRooms(){
        HttpResultProcessListener httpResultProcessListener = new HttpResultProcessListener() {
            @Override
            public void processing(int status, String responsString) {
                if(status == 201){
                    Gson gson = GsonUtil.create();
                    Log.d("query_room", "init_room" + responsString);
                    rooms = gson.fromJson(responsString, Room[].class);
                    if(rooms == null || rooms.length == 0) return;
                    for(int i = 0; i < rooms.length; i++){
                        if(rooms[i].getRoomId().equals(room_id)){
                            pos = i;
                        }
                    }
                    if(user_id != 0)
                        initViewPager();
                }
            }
        };
        AppliancesUpdateResolver.query_room(this, user_id, httpResultProcessListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("life_cycle", "ApplianceActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance);
        findViews();
        initActionBar();
        if(user_id == 0)
            initViewPager();
        initRooms();
    }

}
