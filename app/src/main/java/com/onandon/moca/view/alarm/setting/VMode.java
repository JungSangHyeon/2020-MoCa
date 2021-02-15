package com.onandon.moca.view.alarm.setting;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.ModeManager;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoButtonView;
import com.onandon.moca.onAndOn.oButton.oActionButton.OVectorAnimationActionButton;
import com.onandon.moca.technical.TAlarm;

public class VMode implements View.OnClickListener, View.OnTouchListener {

    // Working Variable
    private int selectedMode;

    // Associate
    private MAlarm mAlarm;
    private TAlarm tAlarm;
    private TextView info;
    private OVectorAnimationActionButton button;

    // Constructor
    public VMode(View view, MAlarm mAlarm, TAlarm tAlarm) {
        this.mAlarm=mAlarm;
        this.tAlarm=tAlarm;

        OTitleInfoButtonView modeView = view.findViewById(R.id.alarm_setting_mode);
        this.info = modeView.getInfo();
        this.button = modeView.getButton();

        this.selectedMode = mAlarm.getMode();

        this.info.setText(Constant.EAlarmMode.values()[this.selectedMode].getModeName());
        this.info.setOnClickListener(this);
        this.button.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        this.selectedMode = (this.selectedMode+1 == Constant.EAlarmMode.values().length)? 0:this.selectedMode+1;
        this.mAlarm.setMode(this.selectedMode);
        this.tAlarm.onCreate( new ModeManager(this.info.getContext()).getMAlarmMode(this.selectedMode));
        this.info.setText(Constant.EAlarmMode.values()[this.selectedMode].getModeName());
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.tAlarm.onStartCommand(); break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.tAlarm.onStopCommand(); break;
        }
        return true;
    }
}
