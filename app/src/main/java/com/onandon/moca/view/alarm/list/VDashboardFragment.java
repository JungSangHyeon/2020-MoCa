package com.onandon.moca.view.alarm.list;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.onandon.moca.R;
import com.onandon.moca.control.MViewModel;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.onAndOn.OAnimator;
import com.onandon.moca.onAndOn.OSimpleAnimatorListener;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.technical.TAlarmManager;
import com.onandon.moca.technical.TTime;
import com.onandon.moca.technical.device.TEarphone;
import com.onandon.moca.view.alarm.setting.VTime;

import java.util.Vector;

public class VDashboardFragment extends Fragment {

    // Constant
    private enum EMode {eAlarmTime, eLeftTime}

    // Working Variable
    private EMode mode;
    private long nowAlarmTime;

    // Associate
        // View
        private TextView time, date, dayOfWeek, name, earphone;
        private OVectorAnimationToggleButton onOffButton;
        // Model
        private MViewModel model;
        private Vector<MAlarm> mAlarms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associate Attribute
        this.loadMode();

        // Associate Model
        this.model = new ViewModelProvider(this.requireActivity()).get(MViewModel.class);
        this.model.onCreate(this.getContext());
        this.mAlarms = this.model.getMAlarms();

        // Set Model Callback
        this.model.getMAlarmsLiveData().observe(this, newMAlarms -> this.updateWithAnimation());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Associate View
        this.name = view.findViewById(R.id.name);
        this.earphone = view.findViewById(R.id.earphone);
        this.time = view.findViewById(R.id.time);
        this.date = view.findViewById(R.id.date);
        this.dayOfWeek = view.findViewById(R.id.day_of_week);
        this.onOffButton = view.findViewById(R.id.on_off_switch);

        // Set View Callback
        view.setOnClickListener((v)->this.editNextAlarm());
        view.setOnLongClickListener((v)->this.removeNextAlarm());
        this.time.setOnClickListener((v)->this.changeMode());
        this.onOffButton.setOnCheckedChangeListener((v, isChecked)->this.changeChecked(isChecked));

    }
    @Override
    public void onResume() {
        super.onResume();
        this.update();
    }

    /**
     * Update
     */
    public void updateWithAnimation() {
        MAlarm nextAlarm = this.model.getNextCloneAlarm();
        if((nextAlarm!=null && nextAlarm.getAlarmTime()!=this.nowAlarmTime) || nextAlarm==null){
            Animator.AnimatorListener animatorListener = new OSimpleAnimatorListener() {
                @Override public void onAnimationEnd(Animator animation) {
                    update(); OAnimator.animateAlphaChange(300, 1, null, time, date, dayOfWeek, name, onOffButton);
            }};
            OAnimator.animateAlphaChange(300, 0, animatorListener, this.time, this.date, this.dayOfWeek, this.name, this.onOffButton);
        }
    }
    public void update() { // must be observed
        MAlarm nextAlarm = this.model.getNextCloneAlarm();
        if(nextAlarm!=null){
            this.nowAlarmTime = nextAlarm.getAlarmTime();
            this.getView().setOnClickListener((v)->this.editNextAlarm());
            this.getView().setOnLongClickListener((v)->this.removeNextAlarm());
            this.name.setText(nextAlarm.getName());
            this.earphone.setVisibility(TEarphone.isEarphoneConnected(this.getContext())? View.VISIBLE : View.INVISIBLE);
            if(this.mode == EMode.eAlarmTime){ this.time.setText(TTime.format(VTime.TIME_PATTERN, this.nowAlarmTime)); }
            else if(this.mode == EMode.eLeftTime){ this.time.setText(TTime.getLeftTimeFromNow(this.nowAlarmTime, this.getResources())); }
            this.date.setText(nextAlarm.getTime().format(VTime.DAY_PATTERN));
            this.dayOfWeek.setText(nextAlarm.getTime().format(VTime.DAY_OF_WEEK_PATTERN));
            this.onOffButton.setVisibility(View.VISIBLE);
            this.onOffButton.setEnabled(true);
            this.onOffButton.setOnCheckedChangeListener(null);
            this.onOffButton.setCheckedWithoutAnimation(nextAlarm.isChecked());
            this.onOffButton.setOnCheckedChangeListener((v, isChecked)->this.changeChecked(isChecked));
        }else{
            this.nowAlarmTime = -1;
            this.getView().setOnClickListener(null);
            this.getView().setOnLongClickListener(null);
            this.name.setText("");
            this.earphone.setVisibility(View.INVISIBLE);
            this.time.setText(this.getResources().getString(R.string.no_alarm));
            this.date.setText("");
            this.dayOfWeek.setText("");
            this.onOffButton.setVisibility(View.GONE);
            this.onOffButton.setEnabled(false);
        }
    }

    /**
     * Callbacks
     */
    private void editNextAlarm() {
        Bundle bundle = new Bundle();
        bundle.putInt("targetIndex", this.model.getNextAlarmIndex());
        Navigation.findNavController(this.getView()).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
    }
    private boolean removeNextAlarm(){
        MAlarm target = this.model.getAlarm(this.model.getNextAlarmIndex());
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle(this.getContext().getResources().getString(R.string.delete_alarm_dialog_title));
        dialog.setMessage(target.getName()+" "+this.getContext().getResources().getString(R.string.delete_alarm_dialog_content));
        dialog.setPositiveButton(this.getContext().getResources().getString(R.string.common_ok), (dialog1, which) -> {
            this.model.removeAlarm(this.model.getNextAlarmIndex());
            this.model.notifyValueUpdated();
        });
        dialog.setNegativeButton(this.getContext().getResources().getString(R.string.common_cancel), null);
        dialog.create().show();
        return true; // for long click return value. consumed
    }
    public void changeMode(){

        this.model.savee();

        this.mode = EMode.values()[(this.mode.ordinal()+1 == EMode.values().length)? 0:this.mode.ordinal()+1];
        this.saveMode();
        this.update();
    }
    public void changeChecked(boolean isChecked){
        if(this.model.getNextAlarmIndex()!=-1){
            this.model.getAlarm(this.model.getNextAlarmIndex()).setChecked(isChecked);
            TAlarmManager.scheduleAlarm(this.getContext(), this.model.getNextCloneAlarm());
            this.model.notifyValueUpdated();
        }
    }

    /**
     * Save & Load Mode
     */
    public void saveMode() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("VNextAlarmInfoMode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("modeEnumIndex", this.mode.ordinal());
        editor.commit();
    }
    public void loadMode() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("VNextAlarmInfoMode", Context.MODE_PRIVATE);
        int modeEnumIndex = prefs.getInt("modeEnumIndex", -1);
        this.mode = (modeEnumIndex!=-1)? EMode.values()[modeEnumIndex]:EMode.eAlarmTime;
    }
}
