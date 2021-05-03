package com.onandon.moca.domain.view.alarm.setting;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.OFragment;
import com.onandon.moca.domain.view.alarm.setting.comp.VEarphone;
import com.onandon.moca.domain.view.alarm.setting.comp.VMode.VMode;
import com.onandon.moca.domain.view.alarm.setting.comp.VName;
import com.onandon.moca.domain.view.alarm.setting.comp.VReAlarm;
import com.onandon.moca.domain.view.alarm.setting.comp.VTime;

import java.util.List;

public class VAlarmSetting extends OFragment<AlarmViewModel> {

    // Constant
    private static final int NewAlarm = -1;

    // Attribute
    private int targetIndex;

    // Working Variable
    private MAlarmData targetMAlarmData;

    // Component
    private VName vName;
    private VTime vTime;
    private VReAlarm vReAlarm;
    private VMode vMode;
    private VEarphone vEarphone;

    /**
     * System Callback
     */
    @Override
    protected void createComponent() {
        this.vName = new VName();
        this.vTime = new VTime();
        this.vReAlarm = new VReAlarm();
        this.vMode = new VMode();
        this.vEarphone = new VEarphone();
    }
    @Override
    public void onCreate(Activity activity) {
        this.targetIndex = this.getArguments().getInt("targetIndex");

        this.vName.onCreate(activity);
        this.vTime.onCreate(activity);
        this.vReAlarm.onCreate(activity);
        this.vMode.onCreate(activity);
        this.vEarphone.onCreate(activity);
    }
    @Override public ViewModelStoreOwner getViewModelStoreOwner() { return this.requireActivity(); }
    @Override public Class<? extends ViewModel> getModel() {return AlarmViewModel.class; }
    @Override public int getLayoutId() { return R.layout.alarm_setting; }
    @Override
    public void associateAndInitView(View view) {
        this.vName.associateAndInitView(view);
        this.vTime.associateAndInitView(view);
        this.vReAlarm.associateAndInitView(view);
        this.vMode.associateAndInitView(view);
        this.vEarphone.associateAndInitView(view);

        this.vTime.createModel(this);
        view.findViewById(R.id.alarm_setting_save).setOnClickListener(v->onSaveClick());
        view.findViewById(R.id.alarm_setting_cancel).setOnClickListener(v-> goBack());
    }

    /**
     * Update
     */
    @Override
    protected void update() {
        this.targetMAlarmData = (this.targetIndex == NewAlarm)? this.newMAlarm() : this.getAlarms().get(this.targetIndex).getMAlarm().clone();

        this.vName.update(this.targetMAlarmData);
        this.vTime.update(this.targetMAlarmData);
        this.vReAlarm.update(this.targetMAlarmData);
        this.vMode.update(this.targetMAlarmData);
        this.vEarphone.update();
    }

    /**
     * Callback
     */
    public void onSaveClick(){
        this.targetMAlarmData.setName(this.vName.getName().equals("")? "Alarm "+this.getNameNumber():this.vName.getName());
        if (this.targetIndex == NewAlarm) {
            Alarm alarm = new Alarm();
            alarm.setMAlarm(this.targetMAlarmData);
            alarm.setIndex(this.getAlarms().size()==0? 0:this.findNextIndex());
            this.model.insert(alarm);
        } else {
            Alarm alarm = this.getAlarms().get(this.targetIndex);
            alarm.setMAlarm(this.targetMAlarmData);
            this.model.update(alarm);
        }
        this.goBack();
    }

    private int findNextIndex() {
        int index = 0;
        for(Alarm alarm : this.getAlarms()){
            if(index <= alarm.getIndex()){
                index = alarm.getIndex()+1;
            }
        }
        return index;
    }

    public void goBack(){
        this.removeModelObserver();
        Navigation.findNavController(this.getView()).popBackStack();
    }

    /**
     * ETC
     */
    private MAlarmData newMAlarm() {
        MAlarmData newMAlarmData = new MAlarmData();
        newMAlarmData.setChecked(true);
        newMAlarmData.setName("Alarm "+this.getNameNumber());
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.requireContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this.requireContext(), defaultRingtoneUri);
        newMAlarmData.getRingtone().setName(defaultRingtone.getTitle(this.requireContext()));
        newMAlarmData.getRingtone().setUri(defaultRingtoneUri);
        return newMAlarmData;
    }
    private int getNameNumber() {
        int num = 0;
        if(this.getAlarms()==null) {return num;}
        boolean noSameName = false;
        while(!noSameName){
            noSameName = true;
            for(Alarm alarm : this.getAlarms()){
                if(alarm.getMAlarm().getName().equals("Alarm "+num)){
                    noSameName = false;
                    num++;
                    break;
                }
            }
        }
        return num;
    }
    public List<Alarm> getAlarms(){ return this.model.getAlarms().getValue(); }
}
