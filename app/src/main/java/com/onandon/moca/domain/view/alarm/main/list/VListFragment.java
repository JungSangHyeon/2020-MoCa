package com.onandon.moca.domain.view.alarm.main.list;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.OFragment;
import com.onandon.moca.onAndOnAsset.technical.device.TVibrator;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class VListFragment extends OFragment<AlarmViewModel> {

    // Working Variable
    private boolean alarmDeleteAction = false;
    private Vector<Alarm> cloneAlarmList;

    // Associate
        // View
        private RecyclerView recyclerView;

    // Component
    private VAlarmAdapter vAlarmAdapter;

    @Override
    protected void createComponent() {
        this.vAlarmAdapter = new VAlarmAdapter();
    }
    @Override
    public void onCreate(Activity activity) {
        this.vAlarmAdapter.onCreate((v)->this.onClick(v), (v, e)->this.initAction(e));
    }
    @Override public ViewModelStoreOwner getViewModelStoreOwner() { return this.requireActivity(); }
    @Override public Class<? extends ViewModel> getModel() { return AlarmViewModel.class; }
    @Override public int getLayoutId() { return R.layout.alarm_main_list; }
    @Override
    public void associateAndInitView(View view) {
        this.recyclerView = view.findViewById(R.id.list);

        this.recyclerView.setAdapter(this.vAlarmAdapter);
        new ItemTouchHelper(new OSimpleCallback()).attachToRecyclerView(this.recyclerView);
    }

    /**
     * Update
     */
    @Override
    public void update() {
        this.cloneAlarmList = new Vector<>();
        for (Alarm alarm : this.model.getAlarms().getValue()){ this.cloneAlarmList.add(alarm.clone()); }
        this.vAlarmAdapter.update(this.model, this.cloneAlarmList);
    }

    /**
     * Callback
     */
    public boolean initAction(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){ this.alarmDeleteAction = false; }
        return false;
    }
    public void onClick(View v){
        if(!this.alarmDeleteAction){
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
        private List<Alarm> updateTarget;

        // Constructor
        public OSimpleCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(this.startAction){
                this.updateTarget = model.getAlarms().getValue();
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
                for(Alarm target :  this.updateTarget){ // update move
                    model.update(target);
                }
            }
            this.startAction=!this.startAction;
        }

        @Override // change item position
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if(viewHolder!=target){
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Alarm alarm1 =  this.updateTarget.get(fromPosition);
                Alarm alarm2 =  this.updateTarget.get(toPosition);
                int index1 = alarm1.getIndex();
                int index2 = alarm2.getIndex();
                alarm1.setIndex(index2);
                alarm2.setIndex(index1);
                Log.d("TEST1234", index1+", "+index2+" / "+alarm1.getIndex()+", "+alarm2.getIndex());
                Collections.swap( this.updateTarget, fromPosition, toPosition);
                Collections.swap(cloneAlarmList, fromPosition, toPosition);
                vAlarmAdapter.notifyItemMoved(fromPosition, toPosition);
            }
            return false;
        }
        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
    }
    private boolean removeAlarm(int index){
        Alarm target = this.model.getAlarms().getValue().get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle(this.getContext().getResources().getString(R.string.delete_alarm_dialog_title));
        dialog.setMessage(target.getMAlarm().getName()+" "+this.getContext().getResources().getString(R.string.delete_alarm_dialog_content));
        dialog.setPositiveButton(this.getContext().getResources().getString(R.string.common_ok), (dialog1, which) -> this.model.delete(target));
        dialog.setNegativeButton(this.getContext().getResources().getString(R.string.common_cancel), null);
        dialog.create().show();
        return true; // for long click return value. consumed
    }
}
