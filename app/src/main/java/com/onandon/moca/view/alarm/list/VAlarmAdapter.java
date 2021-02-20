package com.onandon.moca.view.alarm.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.MViewModel;

// handling one list item at a time
public class VAlarmAdapter extends RecyclerView.Adapter<VAlarmViewHolder> {

    // Associate
    private View.OnClickListener editListener;
    private View.OnTouchListener actionInitListener;

    // Associate
        // Model
        private MViewModel model;

    // Constructor
    public VAlarmAdapter(MViewModel model, View.OnClickListener editListener, View.OnTouchListener actionInitListener){
        // Associate
        this.model=model;
        this.editListener = editListener;
        this.actionInitListener = actionInitListener;
    }

    @Override // Create new views (invoked by the layout manager)
    public VAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        return new VAlarmViewHolder(view, this.model, this.editListener, this.actionInitListener);
    }

    @Override // Replace the contents of a view (invoked by the layout manager)
    public void onBindViewHolder(VAlarmViewHolder viewHolderAlarm, int position) {
        viewHolderAlarm.setView(position);
    }

    @Override // Return the size of your dataset (invoked by the layout manager)
    public int getItemCount() {
        return this.model.getMAlarms().size();
    }
}

