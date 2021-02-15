package com.onandon.moca.view.alarm.list;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.OAnimator;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.view.alarm.setting.VTime;

// Provide a reference to the views for each data item
public class VAlarmViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

    // Associate
    private CAlarm cAlarm; // for edit and remove on click
    private UpdateCallback updateCallback;
    private TextView time, date, dayOfWeek, name;
    private OVectorAnimationToggleButton aSwitch;

    // Working Variable
    private boolean folded = false;

    // Constructor
    public VAlarmViewHolder(View adapterView,
                            View.OnClickListener actionListener, UpdateCallback updateCallback, View.OnTouchListener onTouchListener) {
        super(adapterView);
        this.updateCallback=updateCallback;

        this.time =  this.itemView.findViewById(R.id.alarm_list_item_time);
        this.name =  this.itemView.findViewById(R.id.alarm_list_item_name);
        this.date =  this.itemView.findViewById(R.id.alarm_list_item_date);
        this.dayOfWeek =  this.itemView.findViewById(R.id.alarm_list_item_dayofweek);
        this.aSwitch = this.itemView.findViewById(R.id.alarm_list_item_switch);

        this.itemView.setOnTouchListener(onTouchListener);
        this.itemView.setOnClickListener(actionListener);
    }

    // load alarm data
    public void setView(CAlarm cAlarm, int position) {
        this.itemView.setTag(position);
        this.cAlarm = cAlarm;

        MAlarm mAlarm = this.cAlarm.getAlarm(this.getAdapterPosition()).schedulerNextAlarm();
        float alpha = (mAlarm.isChecked())? 1:0.3f;
        this.animateEnableChange(mAlarm.isChecked(), 0, alpha);
        this.updateWithAnimation(0);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        float alpha = (isChecked)? 1:0.3f;
        this.animateEnableChange(isChecked, 300, alpha);

        this.cAlarm.editAlarm(this.getAdapterPosition());
        this.cAlarm.getCurrentAlarm().setChecked(isChecked);
        this.cAlarm.saveAlarm(); // 여기서 원래거 삭제하고 맨 뒤에 넣게 됨. 그리고 리스트 업데이트 안 하니 에러 나던 것.
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
        this.updateCallback.update();
    }

    public void updateWithAnimation(int duration) {
        if(this.getAdapterPosition()==-1){return;} // not visible
        MAlarm mAlarm = this.cAlarm.getAlarm(this.getAdapterPosition()).schedulerNextAlarm();
        this.name.setText(mAlarm.getName());
        this.time.setText(mAlarm.getTime().format(VTime.TIME_PATTERN));
        this.date.setText(mAlarm.getTime().format(VTime.DAY_PATTERN));
        this.dayOfWeek.setText(mAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
        this.aSwitch.setOnCheckedChangeListener(null);
        this.aSwitch.setCheckedWithoutAnimation(mAlarm.isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);

        float alpha = 0;
        ValueAnimator heightAnimator = null;
        if(this.getAdapterPosition()==this.cAlarm.getNextAlarmIndex() && !this.folded){ // this mAlarm is next alarm && not folded
            this.folded=true;
            alpha=0;
            OAnimator.animateAlphaChange(duration, 0, null, this.aSwitch);
            heightAnimator = ValueAnimator.ofInt(itemView.getMinimumHeight(), 0);
        }else if(this.getAdapterPosition()!=this.cAlarm.getNextAlarmIndex() && this.folded){ // this mAlarm is not next alarm && folded
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
}
