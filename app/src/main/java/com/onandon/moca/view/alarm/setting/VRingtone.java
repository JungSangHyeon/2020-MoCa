package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.MAlarmMode;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;

public class VRingtone implements View.OnClickListener, Switch.OnCheckedChangeListener {

    // Association
    private View view;
    private MAlarmMode mAlarm;
    private TextView ringtoneName;
    private OVectorAnimationToggleButton aSwitch;

    // Constructor
    public VRingtone(View view, MAlarmMode mAlarm) {
        this.view = view;
        this.mAlarm = mAlarm;

        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_ringtone);

        this.ringtoneName = itemTitleInfoSwitch.getSettingLayout().findViewById(R.id.info);
        this.ringtoneName.setOnClickListener(this);
        this.ringtoneName.setText(this.mAlarm.getRingtone().isChecked()? this.mAlarm.getRingtone().getName():"");

        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setCheckedWithoutAnimation(this.mAlarm.getRingtone().isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarm.getRingtone().setChecked(isChecked);
        this.ringtoneName.setText(isChecked? this.mAlarm.getRingtone().getName():"");
    }

    @Override // For "selectedNameView"
    public void onClick(View view) {
        this.showRingtoneSettingDialog();
    }

    private void showRingtoneSettingDialog() {
        AppCompatActivity activity = (AppCompatActivity) this.view.getContext();
        RingtonePickerDialog.Builder builder =
                new RingtonePickerDialog.Builder(activity, (activity.getSupportFragmentManager()));
        builder.setTitle(R.string.alarm_setting_ringtone_slectiondialog);
        builder.setPositiveButtonText(R.string.common_ok);
        builder.setCancelButtonText(R.string.common_cancel);
        builder.setPlaySampleWhileSelection(true);
        builder.setListener((ringtoneName, ringtoneUri) -> {
            this.mAlarm.getRingtone().setName(ringtoneName);
            this.mAlarm.getRingtone().setUri(ringtoneUri);
            this.mAlarm.getRingtone().setChecked(true);
            this.ringtoneName.setText(this.mAlarm.getRingtone().getName());
            this.aSwitch.setChecked(true);
        });

        if(this.mAlarm.getRingtone().getUri() != null){
            builder.setCurrentRingtoneUri(this.mAlarm.getRingtone().getUri());
        }
        builder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
        builder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);
        builder.show();
    }
}
