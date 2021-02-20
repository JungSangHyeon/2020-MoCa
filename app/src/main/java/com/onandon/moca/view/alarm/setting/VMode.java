package com.onandon.moca.view.alarm.setting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.technical.TAlarm;

public class VMode implements View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    // Working Variable
    private MAlarm mAlarm, cloneAlarm;

    // Associate
    private FragmentActivity activity;
        // View
        private Button testButton;
        private RadioGroup radioGroup;

    // Component
    private TAlarm tAlarm;

    /**
     * System Callback
     */
    public void onCreate(FragmentActivity activity) {
        // Associate
        this.activity=activity;

        // Create Component
        this.tAlarm = new TAlarm(this.activity);
    }
    public void onViewCreated(View view){
        // Associate View
        this.testButton = view.findViewById(R.id.alarm_setting_mode_test);
        this.radioGroup = view.findViewById(R.id.alarm_setting_mode_radio);
        RadioButton userDefinedButton = view.findViewById(R.id.alarm_setting_mode_radio_userdefined);

        // Set View Callback
        this.testButton.setOnTouchListener(this);
        this.radioGroup.setOnCheckedChangeListener(this);
        userDefinedButton.setOnClickListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarm mAlarm){
        this.mAlarm=mAlarm;
        this.cloneAlarm = this.mAlarm.clone();

        this.tAlarm.setTargetMAlarm(this.mAlarm);
        this.radioGroup.check(Constant.EAlarmMode.values()[this.mAlarm.getMode()].getRadioButtonId());
    }

    /**
     * Callback
     */
    @Override
    public void onClick(View v) {
        this.showUserDefinedModeSettingDialog();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for(Constant.EAlarmMode mode : Constant.EAlarmMode.values()){
            if(mode.getRadioButtonId() == checkedId){
                this.mAlarm.setMode(mode.ordinal()); break;
            }
        }
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
    public void showUserDefinedModeSettingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.activity);
        LayoutInflater layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogBuilder.setTitle(this.activity.getResources().getString(R.string.user_defined_mode));
        View dialogView = layoutInflater.inflate(R.layout.setting, null);
        dialogBuilder.setView(dialogView);

        TAlarm tAlarm = new TAlarm((Activity) this.activity);
        tAlarm.setTargetMAlarm(this.cloneAlarm);
        new VPower(dialogView, this.cloneAlarm, tAlarm);
        new VRingtone(dialogView, this.cloneAlarm);
        new VVibration(dialogView, this.cloneAlarm);
        new VFlash(dialogView, this.cloneAlarm);
        new VScreen(dialogView, this.cloneAlarm);

        dialogBuilder.setPositiveButton(this.activity.getResources().getString(R.string.common_ok),
                (dialog1, which) -> { this.mAlarm.setAlarmValues(this.cloneAlarm); });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
