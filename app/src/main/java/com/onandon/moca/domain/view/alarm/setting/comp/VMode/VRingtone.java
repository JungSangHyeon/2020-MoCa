package com.onandon.moca.domain.view.alarm.setting.comp.VMode;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView.OTitleInfoSwitchView;

public class VRingtone implements OCustomViewCompLifeCycle, View.OnClickListener, Switch.OnCheckedChangeListener {

    // Association
    private View view;
    private MAlarmData mAlarmData;
    private TextView ringtoneName;
    private OVectorAnimationToggleButton aSwitch;

    /**
     * System Callback
     */
    @Override public void onCreate(Activity activity) { }
    @Override public void associateAndInitView(View view){
        this.view = view;
        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_ringtone);
        this.ringtoneName = itemTitleInfoSwitch.getSettingLayout().findViewById(R.id.info);
        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();

        this.ringtoneName.setOnClickListener(this);
        this.aSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData) {
        this.mAlarmData = mAlarmData;

        this.ringtoneName.setText(this.mAlarmData.getRingtone().isChecked()? this.mAlarmData.getRingtone().getName():"");
        this.aSwitch.setCheckedWithoutAnimation(this.mAlarmData.getRingtone().isChecked());
    }

    /**
     * Callback
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarmData.getRingtone().setChecked(isChecked);
        this.ringtoneName.setText(isChecked? this.mAlarmData.getRingtone().getName():"");
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
            this.mAlarmData.getRingtone().setName(ringtoneName);
            this.mAlarmData.getRingtone().setUri(ringtoneUri);
            this.mAlarmData.getRingtone().setChecked(true);
            this.ringtoneName.setText(this.mAlarmData.getRingtone().getName());
            this.aSwitch.setChecked(true);
        });

        if(this.mAlarmData.getRingtone().getUri() != null){
            builder.setCurrentRingtoneUri(this.mAlarmData.getRingtone().getUri());
        }
        builder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
        builder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);
        builder.show();
    }
}
