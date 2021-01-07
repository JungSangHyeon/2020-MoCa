package com.onandon.moca.view;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.onandon.moca.R;
import com.onandon.moca.view.alarm.VAlarm;
import com.onandon.moca.view.stopwatch.VStopWatch;
import com.onandon.moca.view.worldtime.VWorldTime;

import java.util.HashMap;
import java.util.Map;

public class VMain implements View.OnClickListener {

    // Association
    private AppCompatActivity mainActivity;

    // Component
    private Map<Integer, Fragment> fragmentMap; // <R.Id, Fragment>

    // Constructor
    public VMain(AppCompatActivity mainActivity) {
        // mainActivity
        this.mainActivity = mainActivity;

        // Create Component
//        this.fragmentMap = new HashMap<>();
//        this.fragmentMap.put(R.id.main_alarmbutton, new VAlarm(mainActivity));
//        this.fragmentMap.put(R.id.main_worldtimebutton, new VWorldTime(mainActivity));
//        this.fragmentMap.put(R.id.main_stopwatchbutton, new VStopWatch(mainActivity));

        NavHostFragment navHostFragment = (NavHostFragment) this.mainActivity.getSupportFragmentManager().findFragmentById(R.id.main_view);
        NavController navCo = navHostFragment.getNavController();
        this.mainActivity.findViewById(R.id.main_alarmbutton).setOnClickListener((v)->{ navCo.navigate(R.id.action_to_main_alarm); });
        this.mainActivity.findViewById(R.id.main_worldtimebutton).setOnClickListener((v)->{ navCo.navigate(R.id.action_to_main_worldtime); });
        this.mainActivity.findViewById(R.id.main_stopwatchbutton).setOnClickListener((v)->{ navCo.navigate(R.id.action_global_main_stopwatch); });
    }

    public void onCreate(){
//        for (Integer buttonId : this.fragmentMap.keySet()) {
//            this.mainActivity.findViewById(buttonId).setOnClickListener(this);
//        }
//        this.mainActivity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_view, this.fragmentMap.get(R.id.main_alarmbutton))
//                .commit();
    }

    @Override public void onClick(View view) {
//        this.mainActivity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_view, this.fragmentMap.get(view.getId()))
////                .replace(this.fragmentMap.get(view.getId()))
//                .commit();
    }
}

