package com.onandon.moca.view.alarm.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;

import java.util.Vector;

// handling one list item at a time
public class VAlarmAdapter extends RecyclerView.Adapter<VAlarmViewHolder> {

    // Associate
    private View.OnClickListener actionListener;
    private View.OnLongClickListener removeListener;
    private CAlarm cAlarm;

    // Constructor
    public VAlarmAdapter(View.OnClickListener actionListener, View.OnLongClickListener removeListener, CAlarm cAlarm) {
        this.actionListener = actionListener;
        this.removeListener = removeListener;
        this.cAlarm = cAlarm;
    }

    @Override // Create new views (invoked by the layout manager)
    public VAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        return new VAlarmViewHolder(this.actionListener,this.removeListener, view);
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

