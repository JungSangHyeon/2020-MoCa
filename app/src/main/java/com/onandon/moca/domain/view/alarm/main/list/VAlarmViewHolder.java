package com.onandon.moca.domain.view.alarm.main.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.animator.OAnimator;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.domain.view.alarm.setting.comp.VTime;

// Provide a reference to the views for each data item
public class VAlarmViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

    // Associate
        // View
        private TextView time, name, date, dayOfWeek;
        private OVectorAnimationToggleButton aSwitch;
        // Model
        private AlarmViewModel model;
        private Alarm alarm;
        private MAlarmData mAlarmData;

    // Constructor
    public VAlarmViewHolder(View view, View.OnClickListener editListener, View.OnTouchListener actionInitListener) {
        super(view);

        // Associate View
        this.time =  this.itemView.findViewById(R.id.alarm_list_item_time);
        this.name =  this.itemView.findViewById(R.id.alarm_list_item_name);
        this.date =  this.itemView.findViewById(R.id.alarm_list_item_date);
        this.dayOfWeek =  this.itemView.findViewById(R.id.alarm_list_item_dayofweek);
        this.aSwitch = this.itemView.findViewById(R.id.alarm_list_item_switch);

        // Set View Callback
        this.itemView.setOnTouchListener(actionInitListener);
        this.itemView.setOnClickListener(editListener);
    }
    public void setView(AlarmViewModel model) {
        // Associate Model
        this.model=model;

        this.alarm = this.model.getAlarm(this.getAdapterPosition());
        this.mAlarmData = this.alarm.getMAlarm();

        OAnimator.animateAlphaChange(0, (this.mAlarmData.isChecked())? 1:0.3f, null, this.time, this.name, this.date, this.dayOfWeek);

        ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
        layoutParams.height = (this.model.getNextAlarmIndex()==this.getAdapterPosition())? 0:this.itemView.getMinimumHeight();
        this.itemView.setLayoutParams(layoutParams);

        this.update();
    }

    /**
     * Update
     */
    private void update() {
        MAlarmData mAlarmData = this.mAlarmData.schedulerNextAlarm();
        this.name.setText(mAlarmData.getName());
        this.time.setText(mAlarmData.getTime().format(VTime.TIME_PATTERN));
        this.date.setText(mAlarmData.getTime().format(VTime.DAY_PATTERN));
        this.dayOfWeek.setText(mAlarmData.getTime().format(VTime.DAY_OF_WEEK_PATTERN));
        this.aSwitch.setOnCheckedChangeListener(null);
        this.aSwitch.setCheckedWithoutAnimation(mAlarmData.isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * Callback
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mAlarmData.setChecked(isChecked);
        this.alarm.setMAlarm(mAlarmData);
        this.model.update(alarm);
    }
}
