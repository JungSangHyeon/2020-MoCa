package com.onandon.moca.view.alarm.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.MViewModel;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.technical.device.TVibrator;

import java.util.Collections;
import java.util.Vector;

public class VListFragment extends Fragment {

    // Working Variable
    private boolean alarmDeleteAction = false;

    // Associate
        // View
        private RecyclerView recyclerView;
        // Model
        private MViewModel  model;
        private Vector<MAlarm> mAlarms;

    // Component
    private VAlarmAdapter vAlarmAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associate Model
        this.model = new ViewModelProvider(this.requireActivity()).get(MViewModel.class);
        this.model.onCreate(this.getContext());
        this.mAlarms = this.model.getMAlarms();

        // Set Model Callback
        this.model.getMAlarmsLiveData().observe(this, newMAlarms -> this.update());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_list_list, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Associate View
        this.recyclerView = view.findViewById(R.id.list);

        // Set View Attribute
//        this.recyclerView.setHasFixedSize(true);

        // Set View Component
        this.vAlarmAdapter = new VAlarmAdapter(this.model, (v)->onClick(v), (v,e)->this.initAction(e));
        this.recyclerView.setAdapter(this.vAlarmAdapter);

        // Set View Callback
        new ItemTouchHelper(new OSimpleCallback()).attachToRecyclerView(this.recyclerView);


    }
    @Override
    public void onResume() {
        super.onResume();
        this.update();
    }

    /**
     * Update
     */
    private void update() {
        this.vAlarmAdapter.notifyDataSetChanged();
        for(int i=0; i<this.recyclerView.getChildCount(); i++){
            VAlarmViewHolder viewHolder = (VAlarmViewHolder) this.recyclerView.getChildViewHolder(this.recyclerView.getChildAt(i));
            viewHolder.updateWithAnimation(300);
        }
    }

    /**
     * Callback
     */
    public boolean initAction(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){ alarmDeleteAction = false; }
        return false;
    }
    public void onClick(View v){
        if(!alarmDeleteAction){
            Bundle bundle = new Bundle();
            bundle.putInt("targetIndex", this.recyclerView.getChildAdapterPosition(v));
            Navigation.findNavController(this.getView()).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
        }
    }
    private class OSimpleCallback extends ItemTouchHelper.SimpleCallback {

        // Working Variable
        private RecyclerView.ViewHolder selectedViewHolder;
        private boolean startAction = true;
        private float oldX, oldY;

        // Constructor
        public OSimpleCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(this.startAction){
                this.selectedViewHolder =viewHolder;
                this.oldX = this.selectedViewHolder.itemView.getX();
                this.oldY = this.selectedViewHolder.itemView.getY();
                TVibrator tVibrator = new TVibrator((Activity)getContext());
                tVibrator.onCreate(100);
                tVibrator.start(new int[][]{{100,255}}, -1);
            }else{
                float dX = Math.abs(this.oldX-this.selectedViewHolder.itemView.getX());
                float dY = Math.abs(this.oldY-this.selectedViewHolder.itemView.getY());
                if(dX<=10 && dY <=10){
                    alarmDeleteAction = true;
                    removeAlarm(recyclerView.getChildAdapterPosition(this.selectedViewHolder.itemView));
                }
            }
            this.startAction=!this.startAction;
        }
        @Override // change item position
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(mAlarms, fromPosition, toPosition);
            model.notifyValueUpdated();
//            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }
        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
    }
    private boolean removeAlarm(int index){
        MAlarm target = this.model.getAlarm(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle(this.getContext().getResources().getString(R.string.delete_alarm_dialog_title));
        dialog.setMessage(target.getName()+" "+this.getContext().getResources().getString(R.string.delete_alarm_dialog_content));
        dialog.setPositiveButton(this.getContext().getResources().getString(R.string.common_ok), (dialog1, which) -> {
            this.model.removeAlarm(index);
            this.model.notifyValueUpdated();
        });
        dialog.setNegativeButton(this.getContext().getResources().getString(R.string.common_cancel), null);
        dialog.create().show();
        return true; // for long click return value. consumed
    }
}
