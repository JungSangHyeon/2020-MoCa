package com.onandon.moca.view.alarm.setting;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VRingtone implements View.OnClickListener, Switch.OnCheckedChangeListener {

    // Associations
    private final View view;
    private final MAlarm mAlarm;

    private final TextView ringtoneName;
    private SwitchMaterial aSwitch;

    public VRingtone(View view, MAlarm mAlarm) {
        this.view = view;
        this.mAlarm = mAlarm;

        TextView ringtoneTitle = view.findViewById(R.id.alarm_setting_ringtone_title);
        ringtoneTitle.setOnClickListener(this);

        this.ringtoneName = view.findViewById(R.id.alarm_setting_ringtone_name);
        this.ringtoneName.setText(this.mAlarm.getRingtone().getName());
        this.ringtoneName.setOnClickListener(this);

        this.aSwitch = view.findViewById(R.id.alarm_setting_ringtone_on);
        this.aSwitch.setChecked(this.mAlarm.getRingtone().isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);

        Log.d("VRingtone::VRingtone", Boolean.toString(this.aSwitch.isChecked()));
        Log.d("cAlarmManager::getAlarm", "id:"+ this.mAlarm.getId());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarm.getRingtone().setChecked(isChecked);
        Log.d("VHoliday::onChecked", Boolean.toString(this.mAlarm.getRingtone().isChecked()));
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
