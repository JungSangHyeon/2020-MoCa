package com.onandon.moca.view.alarm.setting;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.TAlarm;

public class VAlarmSetting extends Fragment implements View.OnClickListener {

    // Associate
    private final View.OnClickListener vAlarm;
    private final CAlarm cAlarm;
    private MAlarm mAlarm;
    private VName vName;

    // Constructor
    public VAlarmSetting(View.OnClickListener vAlarm, CAlarm cAlarm) {
        super(R.layout.alarm_setting);
        this.vAlarm = vAlarm;
        this.cAlarm = cAlarm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_setting, container, false);

        this.mAlarm = this.cAlarm.getAlarm();
        this.setDefaultValues();
        TAlarm tAlarm = new TAlarm(this.getActivity());
        tAlarm.onCreate(this.mAlarm);

        this.vName = new VName(view, mAlarm);
        new VTime(view, mAlarm);
        new VPower(view, mAlarm, tAlarm);
        new VRingtone(view, mAlarm);
        new VVibration(view, mAlarm);
        new VFlash(view, mAlarm);
        new VScreen(view, mAlarm);
        new VReAlarm(view, mAlarm);

        Button saveBtn = view.findViewById(R.id.alarm_setting_save);
        saveBtn.setOnClickListener(this);
        Button cancelBtn = view.findViewById(R.id.alarm_setting_cancel);
        cancelBtn.setOnClickListener(this);
        return view;
    }

    private void setDefaultValues() {
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
            this.cAlarm.saveAlarm();
            this.cAlarm.store();
            this.cAlarm.scheduleAlarm();
        }
        this.vAlarm.onClick(view);
    }
}
