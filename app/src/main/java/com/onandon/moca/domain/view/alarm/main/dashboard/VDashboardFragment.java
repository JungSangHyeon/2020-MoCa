package com.onandon.moca.domain.view.alarm.main.dashboard;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.animator.OAnimator;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.OFragment;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOnAsset.utility.UTime;
import com.onandon.moca.onAndOnAsset.technical.device.TEarphone;
import com.onandon.moca.domain.view.alarm.setting.comp.VTime;

public class VDashboardFragment extends OFragment<AlarmViewModel> {

    // Constant
    private enum EMode {eAlarmTime, eLeftTime}
    private static final String ModeEnumIndexKey = "modeEnumIndex";
    private static final int NullMode = -1;
    private static final int NullAlarmTime = -1;

    // Attribute
    private static final int AnimationDuration = 300; // ms

    // Working Variable
    private EMode mode;
    private long nowAlarmTime;

    // Associate
        // View
        private TextView time, date, dayOfWeek, name, earphone;
        private OVectorAnimationToggleButton onOffButton;

    /**
     * System Callback
     */
    @Override protected void createComponent() { }
    @Override public void onCreate(Activity activity) { this.loadMode();}
    @Override public ViewModelStoreOwner getViewModelStoreOwner() { return this.requireActivity(); }
    @Override public Class<? extends ViewModel> getModel() { return AlarmViewModel.class; }
    @Override public int getLayoutId() { return R.layout.alarm_main_dashboard; }
    @Override
    public void associateAndInitView(View view) {
        this.name = view.findViewById(R.id.name);
        this.earphone = view.findViewById(R.id.earphone);
        this.time = view.findViewById(R.id.time);
        this.date = view.findViewById(R.id.date);
        this.dayOfWeek = view.findViewById(R.id.day_of_week);
        this.onOffButton = view.findViewById(R.id.on_off_switch);

        view.setOnClickListener((v)->this.editNextAlarm());
        view.setOnLongClickListener((v)->this.removeNextAlarm());
        this.time.setOnClickListener((v)->this.changeMode());
        this.onOffButton.setOnCheckedChangeListener((v, isChecked)-> this.changeChecked(isChecked));
    }

    /**
     * Update
     */
    @Override
    public boolean isAnimationStartCondition(){
        Alarm nextAlarm = this.model.getNextAlarm();
        boolean case1 = this.nowAlarmTime !=NullAlarmTime && nextAlarm == null; // !null->null
        boolean case2 = nextAlarm!=null && nextAlarm.getMAlarm().schedulerNextAlarm().getAlarmTime() != this.nowAlarmTime; // !null->!null
        return case1 || case2;
    }
    @Override
    public void startBeforeUpdateAnimation(Animator.AnimatorListener startAfterUpdateAnimationListener){
        OAnimator.animateAlphaChange(AnimationDuration, 0, startAfterUpdateAnimationListener,
                this.earphone, this.time, this.date, this.dayOfWeek, this.name, this.onOffButton);
    }
    @Override
    public void startAfterUpdateAnimation(){
        OAnimator.animateAlphaChange(AnimationDuration, 1, null,
                this.earphone, this.time, this.date, this.dayOfWeek, this.name, this.onOffButton);
    }
    @Override
    public void update() {
        Alarm nextAlarm = this.model.getNextAlarm();
        boolean nullAlarm = nextAlarm==null;
        MAlarmData nextMAlarmData = nullAlarm? null : nextAlarm.getMAlarm().schedulerNextAlarm();
        this.nowAlarmTime = nullAlarm? NullAlarmTime : nextMAlarmData.getAlarmTime();
        this.getView().setOnClickListener(nullAlarm? null : (v)->this.editNextAlarm());
        this.getView().setOnLongClickListener(nullAlarm? null : (v)->this.removeNextAlarm());
        this.name.setText(nullAlarm? "" : nextMAlarmData.getName());
        this.earphone.setVisibility(nullAlarm? View.INVISIBLE : TEarphone.isEarphoneConnected(this.getContext())? View.VISIBLE : View.INVISIBLE);
        if(nextAlarm!=null){
            if(this.mode == EMode.eAlarmTime){ this.time.setText(UTime.format(VTime.TIME_PATTERN, this.nowAlarmTime)); }
            else if(this.mode == EMode.eLeftTime){ this.time.setText(UTime.getLeftTimeFromNow(this.nowAlarmTime, this.getResources())); }
        }else{
            this.time.setText(this.getResources().getString(R.string.no_alarm));
        }
        this.date.setText(nullAlarm? "" : nextMAlarmData.getTime().format(VTime.DAY_PATTERN));
        this.dayOfWeek.setText(nullAlarm? "" : nextMAlarmData.getTime().format(VTime.DAY_OF_WEEK_PATTERN));
        this.onOffButton.setVisibility(nullAlarm? View.GONE : View.VISIBLE);
        this.onOffButton.setEnabled(nullAlarm? false : true);
        this.onOffButton.setOnCheckedChangeListener(null);
        this.onOffButton.setCheckedWithoutAnimation(nullAlarm? false : nextMAlarmData.isChecked());
        this.onOffButton.setOnCheckedChangeListener(nullAlarm? null : (v, isChecked)-> this.changeChecked(isChecked));
    }

    /**
     * Callbacks
     */
    public void changeMode(){
        this.mode = EMode.values()[(this.mode.ordinal()+1 == EMode.values().length)? 0:this.mode.ordinal()+1];
        this.saveMode();
        this.update();
    }
    private void editNextAlarm() {
        Bundle bundle = new Bundle();
        bundle.putInt("targetIndex", this.model.getNextAlarmIndex());
        Navigation.findNavController(this.getView()).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
    }
    private boolean removeNextAlarm(){
        Alarm target = this.model.getNextAlarm();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle(this.getContext().getResources().getString(R.string.delete_alarm_dialog_title));
        dialog.setMessage(target.getMAlarm().getName()+" "+this.getContext().getResources().getString(R.string.delete_alarm_dialog_content));
        dialog.setPositiveButton(this.getContext().getResources().getString(R.string.common_ok), (dialog1, which) -> this.model.delete(target));
        dialog.setNegativeButton(this.getContext().getResources().getString(R.string.common_cancel), null);
        dialog.create().show();
        return true; // for long click return value. consumed
    }
    public void changeChecked(boolean isChecked){
        if(!isChecked){
            Alarm target = this.model.getNextAlarm();
            MAlarmData mAlarmData = target.getMAlarm();
            mAlarmData.setChecked(false);
            target.setMAlarm(mAlarmData);
            this.model.update(target);
        }
    }

    /**
     * Save & Load Mode
     */
    public void saveMode() {
        SharedPreferences prefs = this.getContext().getSharedPreferences(this.getClass().getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ModeEnumIndexKey, this.mode.ordinal());
        editor.commit();
    }
    public void loadMode() {
        SharedPreferences prefs = this.getContext().getSharedPreferences(this.getClass().getSimpleName(), Context.MODE_PRIVATE);
        int modeEnumIndex = prefs.getInt(ModeEnumIndexKey, NullMode);
        this.mode = (modeEnumIndex!=NullMode)? EMode.values()[modeEnumIndex]:EMode.eAlarmTime;
    }
}
