package com.onandon.moca.domain.view.alarm.main.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;

import java.util.Vector;

// handling one list item at a time
public class VAlarmAdapter extends ListAdapter<Alarm, VAlarmViewHolder> {

    // Constant
    public static ItemCallback ItemCallback = new ItemCallback();

    // Associate
    private View.OnClickListener editListener;
    private View.OnTouchListener actionInitListener;
        // Model
        private AlarmViewModel model;

    // Constructor
    public VAlarmAdapter(){ super(ItemCallback); }

    public void onCreate(View.OnClickListener editListener, View.OnTouchListener actionInitListener) {
        this.editListener = editListener;
        this.actionInitListener = actionInitListener;
    }

    public void update(AlarmViewModel model, Vector<Alarm> cloneAlarmList) {
        this.model = model;
        this.submitList(cloneAlarmList);
    }

    @Override // Create new views (invoked by the layout manager)
    public VAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_main_list_item, parent, false);
        return new VAlarmViewHolder(view, this.editListener, this.actionInitListener);
    }

    @Override // Replace the contents of a view (invoked by the layout manager)
    public void onBindViewHolder(VAlarmViewHolder viewHolderAlarm, int position) {
        viewHolderAlarm.setView(this.model);
    }

    public static class ItemCallback extends DiffUtil.ItemCallback<Alarm>{
        @Override
        public boolean areItemsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
            MAlarmData oldMAlarmData = oldItem.getMAlarm();
            MAlarmData newMAlarmData = newItem.getMAlarm();
            boolean result =
                    oldItem.isFold() == newItem.isFold() &&
                    oldMAlarmData.getName().equals(newMAlarmData.getName()) &&
                    oldMAlarmData.getTime().getTimeInMillis() == newMAlarmData.getTime().getTimeInMillis() &&
                    oldMAlarmData.isChecked() == newMAlarmData.isChecked();
            return result;
        }
    }
}

