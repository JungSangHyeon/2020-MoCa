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
import com.onandon.moca.model.ModeManager;
import com.onandon.moca.onAndOn.oButton.oActionButton.OVectorAnimationActionButton;
import com.onandon.moca.technical.TAlarm;

public class VAlarmSetting extends Fragment implements View.OnClickListener {

    // Attribute
    private int mAlarmCount;

    // Associate
    private MAlarm mAlarm;
    private VName vName;
    private View.OnClickListener saveActionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mAlarm = (MAlarm) this.getArguments().getSerializable("MAlarm");
        this.saveActionListener = (View.OnClickListener) this.getArguments().getSerializable("SaveActionListener");
        this.mAlarmCount =this.getArguments().getInt("MAlarmCount");

        View view = inflater.inflate(R.layout.alarm_setting, container, false);

        this.setDefaultValues();
        TAlarm tAlarm = new TAlarm(this.getActivity());
        tAlarm.onCreate(new ModeManager(this.getContext()).getMAlarmMode(this.mAlarm.getMode()));

        this.vName = new VName(view, this.mAlarm);
        new VTime(view, this.mAlarm);
        new VMode(view, this.mAlarm, tAlarm);
        new VReAlarm(view, this.mAlarm);
//        new VRingtone(view, this.mAlarm);
//        new VVibration(view, this.mAlarm);
//        new VFlash(view, this.mAlarm);
//        new VScreen(view, this.mAlarm);
//        new VPower(view, this.mAlarm, tAlarm);
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
            this.saveActionListener.onClick(view);
        }
        this.getActivity().onBackPressed();
    }
}
