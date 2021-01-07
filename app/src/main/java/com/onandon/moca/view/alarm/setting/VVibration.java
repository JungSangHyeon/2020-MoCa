package com.onandon.moca.view.alarm.setting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.device.TVibrator;

public class VVibration implements
        Switch.OnCheckedChangeListener,
        View.OnClickListener
//        RadioGroup.OnCheckedChangeListener
{

    private TextView title;
    private TextView name;
    private SwitchMaterial aSwitch;

    private int selectedPattern;

    private View view;
    private MAlarm mAlarm;
    public VVibration(View view, MAlarm mAlarm) {
        this.view = view;
        this.mAlarm = mAlarm;

        this.selectedPattern = this.mAlarm.getVibration().getPattern();

        this.name = view.findViewById(R.id.alarm_setting_vibration_name);
        this.name.setOnClickListener(this);

        this.aSwitch = view.findViewById(R.id.alarm_setting_vibration_on);
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setChecked(this.mAlarm.getVibration().isVibrationChecked());

        this.title = view.findViewById(R.id.alarm_setting_vibration_title);
        this.title.setOnClickListener((v)->{
            this.aSwitch.setChecked(!this.aSwitch.isChecked());
        });
    }

    @Override
    public void onClick(View v) {
//        this.showVibrationSettingDialog();
        this.selectedPattern = (this.selectedPattern+1 == Constant.VibrationNames.length)? 0:this.selectedPattern+1;
        this.mAlarm.getVibration().setPattern(this.selectedPattern);
        this.name.setText(Constant.VibrationNames[this.selectedPattern]);
        new TVibrator((Activity)this.view.getContext()).start(
                Constant.VibrationTimings[selectedPattern],
                Constant.VibrationAmplitudes[selectedPattern],
                -1);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarm.getVibration().setVibrationChecked(isChecked);
        this.name.setText(isChecked? Constant.VibrationNames[this.mAlarm.getVibration().getPattern()]:"");
    }

//    public void showVibrationSettingDialog() {
//        // Inflate Layout
//        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout dialogView = (LinearLayout) inflater.inflate(R.layout.alarm_setting_vibration_dialog, null);
//
//        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroup);
//        this.selectedPattern = mAlarm.getVibration().getPattern();
//        ((RadioButton) radioGroup.getChildAt(this.selectedPattern)).setChecked(true);
//        radioGroup.setOnCheckedChangeListener(this);
//
//        for(int i=0; i<radioGroup.getChildCount(); i++){
//            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
//            radioButton.setText(Constant.VibrationNames[i]);
//        }
//
//        // Create & Show Dialog
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
//        alertDialog.setTitle(view.getResources().getString(R.string.alarm_setting_vibration_pattern));
//        alertDialog.setView(dialogView);
//
//        alertDialog.setPositiveButton(view.getResources().getString(R.string.common_ok), (dialogInterface, index) -> {
//            mAlarm.getVibration().setPattern(this.selectedPattern);
//            mAlarm.getVibration().setVibrationChecked(true);
//            this.name.setText(Constant.VibrationNames[this.selectedPattern]);
//            this.aSwitch.setChecked(true);
//        });
//        alertDialog.setNegativeButton(view.getResources().getString(R.string.common_cancel), (dialogInterface, i) -> {
//        });
//        alertDialog.show();
//    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        this.selectedPattern = group.indexOfChild(group.findViewById(checkedId));
//        new TVibrator((Activity)this.view.getContext()).start(
//                Constant.VibrationTimings[selectedPattern],
//                Constant.VibrationAmplitudes[selectedPattern],
//               -1);
//    }

}

