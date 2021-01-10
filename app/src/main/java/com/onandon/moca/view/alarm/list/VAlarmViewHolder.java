package com.onandon.moca.view.alarm.list;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.alarm.setting.VTime;

// Provide a reference to the views for each data item
public class VAlarmViewHolder extends RecyclerView.ViewHolder
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnLongClickListener {

    // Associate
    private TextView time, date, dayOfWeek, name;
    private Switch aSwitch;
    private View.OnClickListener vAlarm; // for replace fragment on click
    private RecyclerView.Adapter vAlarmAdapter;
    private CAlarm cAlarm; // for edit and remove on click

    // Constructor
    public VAlarmViewHolder(View.OnClickListener vAlarm, VAlarmAdapter vAlarmAdapter, View adapterView) {
        super(adapterView);
        // Associate
        this.vAlarm = vAlarm;
        this.vAlarmAdapter = vAlarmAdapter;

        this.time =  this.itemView.findViewById(R.id.alarm_list_item_time);
        this.name =  this.itemView.findViewById(R.id.alarm_list_item_name);
        this.date =  this.itemView.findViewById(R.id.alarm_list_item_date);
        this.dayOfWeek =  this.itemView.findViewById(R.id.alarm_list_item_dayofweek);
        this.aSwitch = this.itemView.findViewById(R.id.alarm_list_item_on);

        this.itemView.setOnClickListener(this);
        this.itemView.setOnLongClickListener(this);
    }

    // load alarm data
    public void setView(CAlarm cAlarm, int position) {
        this.cAlarm = cAlarm;
        MAlarm mAlarm = this.cAlarm.getAlarm(position).schedulerNextAlarm();
        this.name.setText(mAlarm.getName());
        this.time.setText(mAlarm.getTime().format(VTime.TIME_PATTERN));
        this.date.setText(mAlarm.getTime().format(VTime.DAY_PATTERN));
        this.dayOfWeek.setText(mAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
        this.aSwitch.setChecked(mAlarm.isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = this.getAdapterPosition();
        this.cAlarm.editAlarm(position);
        this.vAlarm.onClick(view);
    }

    // delete item
    @Override
    public boolean onLongClick(View view) {
        this.cAlarm.removeAlarm(this.getAdapterPosition());
        this.cAlarm.store();
        this.vAlarmAdapter.notifyItemRemoved(this.getAdapterPosition());
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.cAlarm.editAlarm(this.getAdapterPosition());
        this.cAlarm.getAlarm().setChecked(isChecked);
        this.cAlarm.saveAlarm();
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
    }
}
