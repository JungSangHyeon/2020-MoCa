package com.onandon.moca.view.alarm.setting;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.oButton.oActionButton.OVectorAnimationActionButton;
import com.onandon.moca.technical.TAlarm;
import com.onandon.moca.view.alarm.list.VAlarmList;

public class VAlarmSetting extends Fragment implements View.OnClickListener {

    // Attribute
    private int mAlarmCount;

    // Associate
    private MAlarm mAlarm;
    private VName vName;
    private SaveFlag saveFlag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mAlarm = (MAlarm) this.getArguments().getSerializable(this.getContext().getResources().getString(R.string.mAlarm));
        this.saveFlag = (SaveFlag) this.getArguments().getSerializable("saveFlag");
        this.mAlarmCount =this.getArguments().getInt(this.getContext().getResources().getString(R.string.mAlarmCount));

        View view = inflater.inflate(R.layout.alarm_setting, container, false);

        this.setDefaultValues();
        TAlarm tAlarm = new TAlarm(this.getActivity());
        tAlarm.onCreate(this.mAlarm);

        this.vName = new VName(view, this.mAlarm);
        new VTime(view, this.mAlarm);
        new VMode(view, this.mAlarm, tAlarm);
        new VReAlarm(view, this.mAlarm);
        new VEarphone(view);

        OVectorAnimationActionButton saveBtn = view.findViewById(R.id.alarm_setting_save);
        saveBtn.setOnClickListener(this);
        OVectorAnimationActionButton cancelBtn = view.findViewById(R.id.alarm_setting_cancel);
        cancelBtn.setOnClickListener(this);
        return view;
    }

    private void setDefaultValues() {
        if(this.mAlarm.getName()==null){
            this.mAlarm.setName("Alarm "+this.mAlarmCount);
        }
        if (this.mAlarm.getRingtone().getUri()==null) {
            Uri defaultRingtoneUri = RingtoneManager
                    .getActualDefaultRingtoneUri(this.getActivity().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
            Ringtone defaultRingtone = RingtoneManager.getRingtone(this.getActivity(), defaultRingtoneUri);
            this.mAlarm.getRingtone().setName(defaultRingtone.getTitle(this.getActivity().getApplicationContext()));
            this.mAlarm.getRingtone().setUri(defaultRingtoneUri);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()  == R.id.alarm_setting_save) {
            this.mAlarm.setName(this.vName.getName());
            this.mAlarm.setChecked(true);
            if(this.mAlarm.getName().equals("")){
                this.mAlarm.setName("Alarm "+this.mAlarmCount);
            }
            saveFlag.setSaved(true);
        }
        this.getActivity().onBackPressed();
    }
}
