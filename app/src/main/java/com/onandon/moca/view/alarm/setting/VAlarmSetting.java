package com.onandon.moca.view.alarm.setting;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.TAlarm;
import com.onandon.moca.view.alarm.VAlarm;
import com.onandon.moca.view.customView.OActionButton;

import java.io.Serializable;

public class VAlarmSetting extends Fragment implements View.OnClickListener {

    // Associate
    private MAlarm mAlarm;
    private VName vName;
    private View.OnClickListener saveActionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mAlarm = (MAlarm) this.getArguments().getSerializable("MAlarm");
        this.saveActionListener = (View.OnClickListener) this.getArguments().getSerializable("SaveActionListener");

        View view = inflater.inflate(R.layout.alarm_setting, container, false);

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

        OActionButton saveBtn = view.findViewById(R.id.alarm_setting_save);
        saveBtn.setOnClickListener(this);
        OActionButton cancelBtn = view.findViewById(R.id.alarm_setting_cancel);
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
            this.saveActionListener.onClick(view);
        }
        this.getActivity().onBackPressed();
    }

}
