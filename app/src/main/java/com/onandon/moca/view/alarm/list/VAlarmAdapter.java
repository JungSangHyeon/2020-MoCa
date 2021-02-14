package com.onandon.moca.view.alarm.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;

// handling one list item at a time
public class VAlarmAdapter extends RecyclerView.Adapter<VAlarmViewHolder> {

    // Associate
    private View.OnClickListener actionListener;
    private CAlarm cAlarm;
    private UpdateCallback updateCallback;
    private View.OnTouchListener onTouchListener;

    // Constructor
    public VAlarmAdapter(CAlarm cAlarm, View.OnClickListener editListener,UpdateCallback updateCallback, View.OnTouchListener onTouchListener){
        this.cAlarm = cAlarm;
        this.actionListener = editListener;
        this.updateCallback = updateCallback;
        this.onTouchListener = onTouchListener;
    }

    @Override // Create new views (invoked by the layout manager)
    public VAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        return new VAlarmViewHolder(view, this.actionListener, this.updateCallback, this.onTouchListener);
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

