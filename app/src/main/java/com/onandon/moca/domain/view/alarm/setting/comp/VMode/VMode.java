package com.onandon.moca.domain.view.alarm.setting.comp.VMode;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.onandon.moca.domain.Constant;
import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.domain.technical.TAlarm;

public class VMode implements OCustomViewCompLifeCycle, View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    // Working Variable
    private MAlarmData mAlarmData;

    // Associate
    private Activity activity;
        // View
        private Button testButton;
        private RadioGroup radioGroup;

    // Component
    private TAlarm tAlarm;
    private VFlash vFlash;
    private VPower vPower;
    private VRingtone vRingtone;
    private VVibration vVibration;
    private VScreen vScreen;

    /**
     * System Callback
     */
    // Constructor
    public VMode() {
        this.tAlarm = new TAlarm();
        this.vPower = new VPower();
        this.vFlash = new VFlash();
        this.vRingtone = new VRingtone();
        this.vVibration = new VVibration();
        this.vScreen = new VScreen();
    }
    @Override
    public void onCreate(Activity activity) {
        this.activity=activity;
        this.tAlarm.onCreate(activity);
        this.vPower.onCreate(activity);
        this.vFlash.onCreate(activity);
        this.vRingtone.onCreate(activity);
        this.vVibration.onCreate(activity);
        this.vScreen.onCreate(activity);
    }
    @Override
    public void associateAndInitView(View view){
        this.testButton = view.findViewById(R.id.alarm_setting_mode_test);
        this.radioGroup = view.findViewById(R.id.alarm_setting_mode_radio);
        RadioButton userDefinedButton = view.findViewById(R.id.alarm_setting_mode_radio_userdefined);

        this.testButton.setOnTouchListener(this);
        this.radioGroup.setOnCheckedChangeListener(this);
        userDefinedButton.setOnClickListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData){
        this.mAlarmData = mAlarmData;

        this.tAlarm.setTargetMAlarm(this.mAlarmData);
        this.radioGroup.check(Constant.EAlarmMode.values()[this.mAlarmData.getMode()].getRadioButtonId());
        this.radioGroup.jumpDrawablesToCurrentState();
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
                this.mAlarmData.setMode(mode.ordinal()); break;
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
        View dialogView = layoutInflater.inflate(R.layout.alarm_setting_mode_dialog, null);
        dialogBuilder.setView(dialogView);
        this.vPower.associateAndInitView(dialogView);
        this.vFlash.associateAndInitView(dialogView);
        this.vRingtone.associateAndInitView(dialogView);
        this.vVibration.associateAndInitView(dialogView);
        this.vScreen.associateAndInitView(dialogView);

        this.vPower.update(this.mAlarmData, this.tAlarm);
        this.vFlash.update(this.mAlarmData);
        this.vRingtone.update(this.mAlarmData);
        this.vVibration.update(this.mAlarmData);
        this.vScreen.update(this.mAlarmData);

        dialogBuilder.setPositiveButton(this.activity.getResources().getString(R.string.common_ok), null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
