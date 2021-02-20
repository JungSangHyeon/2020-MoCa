package com.onandon.moca.view.alarm.setting;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.onandon.moca.R;
import com.onandon.moca.control.MViewModel;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;

import java.util.Vector;

public class VAlarmSetting extends Fragment implements View.OnClickListener {

    // Constant
    private static final int NewAlarm = -1;

    // Attribute
    private int targetIndex;

    // Working Variable
    private MAlarm targetMAlarm;

    // Associate
        // Model
        private MViewModel model;
        private Vector<MAlarm> mAlarms;

    // Component
    private VName vName;
    private VTime vTime;
    private VReAlarm vReAlarm;
    private VMode vMode;
    private VEarphone vEarphone;

    // Constructor
    public VAlarmSetting() { super(); this.initialize(); }
    public VAlarmSetting(int contentLayoutId) { super(contentLayoutId); this.initialize(); }
    private void initialize() {
        // Create Component
        this.vName = new VName();
        this.vTime = new VTime();
        this.vReAlarm = new VReAlarm();
        this.vMode = new VMode();
        this.vEarphone = new VEarphone();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associate Model
        this.model = new ViewModelProvider(this.requireActivity()).get(MViewModel.class);
        this.model.onCreate(this.getContext());
        this.mAlarms = this.model.getMAlarms();

        // Set Attribute
        this.targetIndex = this.getArguments().getInt("targetIndex");
        this.targetMAlarm = (this.targetIndex == NewAlarm)? this.newMAlarm() : this.model.getAlarm(this.targetIndex).clone();

        this.vName.onCreate(this.requireActivity());
        this.vTime.onCreate(this.requireActivity());
        this.vReAlarm.onCreate(this.requireActivity());
        this.vMode.onCreate(this.requireActivity());
        this.vEarphone.onCreate(this.requireActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_setting, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.vName.onViewCreated(view);
        this.vTime.onViewCreated(view);
        this.vReAlarm.onViewCreated(view);
        this.vMode.onViewCreated(view);
        this.vEarphone.onViewCreated(view);

        view.findViewById(R.id.alarm_setting_save).setOnClickListener(this);
        view.findViewById(R.id.alarm_setting_cancel).setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        this.update();
    }

    /**
     * Update
     */
    private void update() {
        this.vName.update(this.targetMAlarm);
        this.vTime.update(this.targetMAlarm);
        this.vReAlarm.update(this.targetMAlarm);
        this.vMode.update(this.targetMAlarm);
        this.vEarphone.update(this.targetMAlarm);
    }

    /**
     * Callback
     */
    @Override
    public void onClick(View view) {
        this.targetMAlarm.setName(this.vName.getName().equals("")? "Alarm "+this.getNameNumber():this.vName.getName());
        if (view.getId()  == R.id.alarm_setting_save) {
            if(this.targetIndex == NewAlarm){ this.mAlarms.add(this.targetMAlarm); }
            else{ this.mAlarms.set(this.targetIndex, this.targetMAlarm); }
        }
        this.getActivity().onBackPressed();
    }

    /**
     * ETC
     */
    private MAlarm newMAlarm() {
        MAlarm newMAlarm = new MAlarm();
        newMAlarm.setChecked(true);
        newMAlarm.setName("Alarm "+this.getNameNumber());
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.getActivity().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this.getActivity(), defaultRingtoneUri);
        newMAlarm.getRingtone().setName(defaultRingtone.getTitle(this.getActivity().getApplicationContext()));
        newMAlarm.getRingtone().setUri(defaultRingtoneUri);
        return newMAlarm;
    }
    private int getNameNumber() {
        int num = 0;
        boolean noSameName = false;
        while(!noSameName){
            noSameName = true;
            for(MAlarm mAlarm : this.model.getMAlarms()){
                if(mAlarm.getName().equals("Alarm "+num)){
                    noSameName = false;
                    num++;
                    break;
                }
            }
        }
        return num;
    }
}
