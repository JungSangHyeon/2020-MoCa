package com.onandon.moca.view.alarm.list;

import android.view.View;
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
    private TextView time, date, dayOfWeek, name;
    private OVectorAnimationToggleButton aSwitch;
    private CAlarm cAlarm; // for edit and remove on click
    private VNextAlarmInfo vNextAlarmInfo;
    VNextAlarmInfo.VAlarmListUpdateCallback updateCallback;
    // Constructor
    public VAlarmViewHolder(View.OnClickListener actionListener, View.OnLongClickListener removeListener,
                            View adapterView, VNextAlarmInfo vNextAlarmInfo, VNextAlarmInfo.VAlarmListUpdateCallback updateCallback) {
        super(adapterView);
        this.vNextAlarmInfo=vNextAlarmInfo;
        this.updateCallback=updateCallback;

        this.time =  this.itemView.findViewById(R.id.alarm_list_next_alarm_time);
        this.name =  this.itemView.findViewById(R.id.alarm_list_next_alarm_name);
        this.date =  this.itemView.findViewById(R.id.alarm_list_next_alarm_date);
        this.dayOfWeek =  this.itemView.findViewById(R.id.alarm_list_next_alarm_dayofweek);
        this.aSwitch = this.itemView.findViewById(R.id.alarm_list_item_on);

        this.itemView.setOnClickListener(actionListener);
        this.itemView.setOnLongClickListener(removeListener);
    }

    // load alarm data
    public void setView(CAlarm cAlarm, int position) {
        this.itemView.setTag(position);
        this.cAlarm = cAlarm;
        if(position==this.cAlarm.getNextAlarmIndex()){
            itemView.setVisibility(View.GONE);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        MAlarm mAlarm = this.cAlarm.getAlarm(position).schedulerNextAlarm();
        this.name.setText(mAlarm.getName());
        this.time.setText(mAlarm.getTime().format(VTime.TIME_PATTERN));
        this.date.setText(mAlarm.getTime().format(VTime.DAY_PATTERN));
        this.dayOfWeek.setText(mAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
        this.aSwitch.setChecked(mAlarm.isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);
        this.animateEnableChange(mAlarm.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.animateEnableChange(isChecked);
//        this.cAlarm.editAlarm(this.getAdapterPosition());
//        this.cAlarm.getCurrentAlarm().setChecked(isChecked);
//        this.cAlarm.saveAlarm(); // 여기서 원래거 삭제하고 맨 뒤에 넣게 됨. 그리고 리스트 업데이트 안 하니 에러 나던 것.
        this.cAlarm.getAlarm(this.getAdapterPosition()).setChecked(isChecked);
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
        this.updateCallback.update();
//        this.vNextAlarmInfo.update();
    }

    private void animateEnableChange(boolean checked) {
        this.time.setEnabled(checked);
        this.name.setEnabled(checked);
        this.date.setEnabled(checked);
        this.dayOfWeek.setEnabled(checked);
        OAnimator.animateEnableChange(this.time);
        OAnimator.animateEnableChange(this.name);
        OAnimator.animateEnableChange(this.date);
        OAnimator.animateEnableChange(this.dayOfWeek);
        OAnimator.animateEnableChange(this.aSwitch);
    }
}
