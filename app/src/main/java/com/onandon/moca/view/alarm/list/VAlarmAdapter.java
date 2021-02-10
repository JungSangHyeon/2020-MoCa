package com.onandon.moca.view.alarm.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;

import java.util.Vector;

// handling one list item at a time
public class VAlarmAdapter extends RecyclerView.Adapter<VAlarmViewHolder> {

    // Associate
    private View.OnClickListener actionListener;
    private CAlarm cAlarm;
    private VNextAlarmInfo vNextAlarmInfo;
    VNextAlarmInfo.VAlarmListUpdateCallback updateCallback;
    View.OnTouchListener removeListener;

    // Constructor
    public VAlarmAdapter(View.OnClickListener editListener,
                         CAlarm cAlarm, VNextAlarmInfo vNextAlarmInfo, VNextAlarmInfo.VAlarmListUpdateCallback updateCallback){
        this.updateCallback = updateCallback;
        this.actionListener = editListener;
        this.cAlarm = cAlarm;
        this.vNextAlarmInfo = vNextAlarmInfo;
//        ((LinearLayoutManager)this.recyclerView.getLayoutManager()).scrollToPositionWithOffset(1, 0);
    }

    @Override // Create new views (invoked by the layout manager)
    public VAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        return new VAlarmViewHolder(this.actionListener, view, this.vNextAlarmInfo, this.updateCallback);
    }

    @Override // Replace the contents of a view (invoked by the layout manager)
    public void onBindViewHolder(VAlarmViewHolder viewHolderAlarm, int position) {
        viewHolderAlarm.setView(this.cAlarm, position);
    }

    @Override // Return the size of your dataset (invoked by the layout manager)
    public int getItemCount() {
        return this.cAlarm.getAlarmSize();
    }
}

