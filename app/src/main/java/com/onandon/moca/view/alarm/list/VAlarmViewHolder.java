package com.onandon.moca.view.alarm.list;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.MViewModel;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.onAndOn.OAnimator;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.technical.TAlarmManager;
import com.onandon.moca.view.alarm.setting.VTime;

// Provide a reference to the views for each data item
public class VAlarmViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

    // Working Variable
    private boolean folded = false;

    // Associate
        // View
        private TextView time, name, date, dayOfWeek;
        private OVectorAnimationToggleButton aSwitch;
        // Model
        private MViewModel model;

    // Constructor
    public VAlarmViewHolder(View view, MViewModel model, View.OnClickListener editListener, View.OnTouchListener actionInitListener) {
        super(view);

        // Associate Model
        this.model=model;

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
    public void setView(int position) {
        MAlarm mAlarm = this.model.getAlarm(position);
        this.animateEnableChange(mAlarm.isChecked(), 0, (mAlarm.isChecked())? 1:0.3f);
        this.update();
    }

    /**
     * Update
     */
    private void update() {
        MAlarm mAlarm = this.model.getAlarm(this.getAdapterPosition()).schedulerNextAlarm();
        this.name.setText(mAlarm.getName());
        this.time.setText(mAlarm.getTime().format(VTime.TIME_PATTERN));
        this.date.setText(mAlarm.getTime().format(VTime.DAY_PATTERN));
        this.dayOfWeek.setText(mAlarm.getTime().format(VTime.DAY_OF_WEEK_PATTERN));
        this.aSwitch.setOnCheckedChangeListener(null);
        this.aSwitch.setCheckedWithoutAnimation(mAlarm.isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);
    }
    public void updateWithAnimation(int duration) {
        if(this.getAdapterPosition()==-1){return;} // not visible
        this.update();

        MAlarm mAlarm = this.model.getAlarm(this.getAdapterPosition()).schedulerNextAlarm();
        float alpha = 0;
        ValueAnimator heightAnimator = null;
        if(this.getAdapterPosition()==this.model.getNextAlarmIndex() && !this.folded){ // this mAlarm is next alarm && not folded
            this.folded=true;
            alpha=0;
            OAnimator.animateAlphaChange(duration, 0, null, this.aSwitch);
            heightAnimator = ValueAnimator.ofInt(itemView.getMinimumHeight(), 0);
        }else if(this.getAdapterPosition()!=this.model.getNextAlarmIndex() && this.folded){ // this mAlarm is not next alarm && folded
            this.folded=false;
            alpha = (mAlarm.isChecked())? 1:0.3f;
            OAnimator.animateAlphaChange(duration, 1, null, this.aSwitch);
            heightAnimator = ValueAnimator.ofInt(0, itemView.getMinimumHeight());
        }
        if(heightAnimator != null){
            OAnimator.animateAlphaChange(duration, alpha, null, this.time, this.name, this.date, this.dayOfWeek);
            heightAnimator.addUpdateListener(animator -> {
                int val = (Integer) animator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.height = val;
                itemView.setLayoutParams(layoutParams);
            });
            heightAnimator.setDuration(duration);
            heightAnimator.start();
        }
    }
    private void animateEnableChange(boolean checked, int duration, float alpha) {
        this.time.setEnabled(checked);
        this.name.setEnabled(checked);
        this.date.setEnabled(checked);
        this.dayOfWeek.setEnabled(checked);
        OAnimator.animateAlphaChange(duration, alpha, null, this.time, this.name, this.date, this.dayOfWeek);
    }

    /**
     * Callback
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.animateEnableChange(isChecked, 300, (isChecked)? 1:0.3f);

        MAlarm mAlarm = this.model.getAlarm(this.getAdapterPosition());
        mAlarm.setChecked(isChecked);
        TAlarmManager.scheduleAlarm(this.itemView.getContext(), mAlarm);
        this.model.notifyValueUpdated();
    }
}
