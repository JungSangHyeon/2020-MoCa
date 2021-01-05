package com.onandon.moca.view.alarm.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;

// handling one list item at a time
public class VAlarmAdapter
        extends RecyclerView.Adapter<VAlarmViewHolder> {

    private final View.OnClickListener vAlarm;
    private final CAlarm cAlarm;
    private View view;

    public VAlarmAdapter(View.OnClickListener vAlarmMainFragment, CAlarm cAlarm) {
        Log.d("VAlarmAdapter", "VAlarmAdapter");
        this.vAlarm = vAlarmMainFragment;
        this.cAlarm = cAlarm;
    }
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public VAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("VAlarmAdapter", "onCreateViewHolder");
        this.view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_item, parent, false);
        return new VAlarmViewHolder(this.vAlarm,this, view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(VAlarmViewHolder viewHolderAlarm, int position) {
        Log.d("VAlarmAdapter", "onCreateViewHolder");
        viewHolderAlarm.setView(this.cAlarm, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("VAlarmAdapter", "getItemCount");
        return this.cAlarm.getAlarmSize();
    }
}

