package com.onandon.moca.view.alarm.list;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.alarm.setting.VTime;

// Provide a reference to the views for each data item
@RequiresApi(api = Build.VERSION_CODES.N)
public class VAlarmViewHolder
        extends RecyclerView.ViewHolder
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnLongClickListener {

    private final TextView time;
    private final TextView name;
    private final TextView date;
    private final TextView dayOfWeek;
    private final Switch aSwitch;

    // for replace fragment on click
    private View.OnClickListener vAlarm;
    private RecyclerView.Adapter vAlarmAdapter;
    // for edit and remove on click
    private CAlarm cAlarm;
    public VAlarmViewHolder(
            View.OnClickListener vAlarm,
            VAlarmAdapter vAlarmAdapter,
            View adapterView) {

        super(adapterView);
        this.vAlarm = vAlarm;
        this.vAlarmAdapter = vAlarmAdapter;

        this.time =  itemView.findViewById(R.id.alarm_list_item_time);
        this.name =  itemView.findViewById(R.id.alarm_list_item_name);
        this.date =  itemView.findViewById(R.id.alarm_list_item_date);
        this.dayOfWeek =  itemView.findViewById(R.id.alarm_list_item_dayofweek);
        this.aSwitch = itemView.findViewById(R.id.alarm_list_item_on);

        this.itemView.setOnClickListener(this);
        this.itemView.setOnLongClickListener(this);
    }

    // load alarm data
    public void setView(CAlarm cAlarm, int position) {
        Log.d("VAlarmViewHolder::", "setMAlarm-MAlarms Pos "+position);

        this.cAlarm = cAlarm;
        MAlarm mAlarm = this.cAlarm.getAlarm(position);

        this.name.setText(mAlarm.getName());
        String time = mAlarm.getTime().format(VTime.TIME_PATTERN);
        this.time.setText(time);

        MAlarm rescheduledAlarm = mAlarm.schedulerNextAlarm();
        String sDate = rescheduledAlarm.getTime().format(VTime.DAY_PATTERN);
        this.date.setText(sDate);
        String sDayOfWeek = rescheduledAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN);
        this.dayOfWeek.setText(sDayOfWeek);

        Log.d("VAlarmViewHolder: ", "setView" + " " + date);

        this.aSwitch.setChecked(mAlarm.isChecked());
        this.aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("VAlarmViewHolder", "onClick-edit");
        int position = this.getAdapterPosition();
        this.cAlarm.editAlarm(position);
        this.vAlarm.onClick(view);
    }

    // delete item
    @Override
    public boolean onLongClick(View view) {
        Log.d("VAlarmViewHolder", "onLongClick-erase");
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
