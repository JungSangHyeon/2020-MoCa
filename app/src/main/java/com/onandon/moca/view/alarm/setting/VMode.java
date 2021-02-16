package com.onandon.moca.view.alarm.setting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.oButton.oActionButton.OVectorAnimationActionButton;
import com.onandon.moca.technical.TAlarm;

public class VMode implements View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    // Working Variable
    private int selectedMode;

    // Associate
    private Context context;
    private MAlarm mAlarm, cloneAlarm;
    private TAlarm tAlarm;
    private OVectorAnimationActionButton settingButton, testButton;
    private RadioGroup radioGroup;

    // Constructor
    public VMode(View view, MAlarm mAlarm, TAlarm tAlarm) {
        this.context = view.getContext();
        this.mAlarm=mAlarm;
        this.cloneAlarm = this.mAlarm.clone();
        this.tAlarm=tAlarm;

        this.selectedMode = mAlarm.getMode();

        this.settingButton = view.findViewById(R.id.alarm_setting_mode_setting);
        this.testButton = view.findViewById(R.id.alarm_setting_mode_test);
        this.radioGroup = view.findViewById(R.id.alarm_setting_mode_radio);

        this.settingButton.setOnClickListener(this);
        this.testButton.setOnTouchListener(this);
        this.radioGroup.setOnCheckedChangeListener(this);
        this.radioGroup.check(Constant.EAlarmMode.values()[this.mAlarm.getMode()].getRadioButtonId());
    }

    @Override
    public void onClick(View v) {
        // setting btn
//        this.selectedMode = (this.selectedMode+1 == Constant.EAlarmMode.values().length)? 0:this.selectedMode+1;
//        this.mAlarm.setMode(this.selectedMode);
//        this.tAlarm.onCreate( new ModeManager(this.info.getContext()).getMAlarmMode(this.selectedMode));
//        this.info.setText(Constant.EAlarmMode.values()[this.selectedMode].getModeName());
        this.radioGroup.check(R.id.alarm_setting_mode_radio_userdefined);
        this.mAlarm.setMode(Constant.EAlarmMode.eUserDefined.ordinal());
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogBuilder.setTitle("사용자 설정 모드");
        View dialogView = layoutInflater.inflate(R.layout.setting, null);
        dialogBuilder.setView(dialogView);

        TAlarm tAlarm = new TAlarm((Activity) this.context);
        tAlarm.onCreate(this.cloneAlarm);
        new VPower(dialogView, this.cloneAlarm, tAlarm);
        new VRingtone(dialogView, this.cloneAlarm);
        new VVibration(dialogView, this.cloneAlarm);
        new VFlash(dialogView, this.cloneAlarm);
        new VScreen(dialogView, this.cloneAlarm);

//        Button noSoundButton =  dialogView.findViewById(R.id.setting_nosoundbutton);
//        noSoundButton.setText(Constant.EAlarmMode.eNoSound.getModeName());
//        noSoundButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eNoSound.ordinal()); });
//        Button soundButton =  dialogView.findViewById(R.id.setting_soundbutton);
//        soundButton.setText(Constant.EAlarmMode.eSound.getModeName());
//        soundButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eSound.ordinal()); });
//        Button crazyButton =  dialogView.findViewById(R.id.setting_crazybutton);
//        crazyButton.setText(Constant.EAlarmMode.eCrazy.getModeName());
//        crazyButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eCrazy.ordinal()); });
//        Button userDefinedButton =  dialogView.findViewById(R.id.setting_userdefinedbutton);
//        userDefinedButton.setText(Constant.EAlarmMode.eUserDefined.getModeName());
//        userDefinedButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eUserDefined.ordinal()); });

        dialogBuilder.setPositiveButton(this.context.getResources().getString(R.string.common_save), (dialog1, which) -> {
            this.mAlarm.setAlarmValues(this.cloneAlarm);
        });
        dialogBuilder.setNegativeButton(this.context.getResources().getString(R.string.common_cancel), (dialog1, which) -> {
            this.cloneAlarm = this.mAlarm.clone();
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
