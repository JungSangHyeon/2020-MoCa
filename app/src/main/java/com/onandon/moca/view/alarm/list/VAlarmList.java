package com.onandon.moca.view.alarm.list;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.customView.OMovableFloatingActionButton;
import com.onandon.moca.technical.device.TVibrator;

import java.io.Serializable;
import java.util.Collections;
import java.util.Locale;

public class VAlarmList extends Fragment implements View.OnLongClickListener, UpdateCallback, View.OnClickListener, View.OnTouchListener {

    // Working Variable
    private boolean alarmDeleteAction = false;

    // Associate View
    private RecyclerView recyclerView;
    private OMovableFloatingActionButton createAlarmBtn;

    // Component
    private VDashboard vDashboard;
    private VAlarmAdapter vAlarmAdapter;
    private CAlarm cAlarm;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.alarm_list, container, false);

        return  this.view;
    }

    /**
     *  methods for create, edit, save, remove alarm
     */
    private void createAlarm() {
        this.cAlarm.createAlarm();
        this.goToAlarmSetting();
    }
    private void editAlarm(int targetPosition) {
        this.cAlarm.editAlarm(targetPosition);
        this.goToAlarmSetting();
    }
    private void saveAlarm() {
        this.cAlarm.saveAlarm();
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
        this.vDashboard.update();
    }
    private void removeAlarm(int targetPosition){
        this.showRemoveConfirmDialog(targetPosition);
    }

    /**
     * Callbacks
     */
    private class SaveListener implements View.OnClickListener, Serializable { // for save alarm
        @Override public void onClick(View view) { saveAlarm(); }
    }
    @Override
    public boolean onLongClick(View view) { // for remove dashboard alarm
        this.removeAlarm(this.cAlarm.getNextAlarmIndex());
        return true;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) { // for remove item alarm
        if(event.getAction()==MotionEvent.ACTION_DOWN){ alarmDeleteAction = false; }
        return false;
    }
    @Override
    public void onClick(View v) { // for remove item alarm
        if(!alarmDeleteAction){ this.editAlarm(this.recyclerView.getChildAdapterPosition(v)); }
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

        @Override // change item position
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(cAlarm.getMAlarms(), fromPosition, toPosition);
            cAlarm.store();
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
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
        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
    }

    /**
     * update all
     */
    @Override
    public void update() {
        this.vDashboard.updateWithAnimation();
        for(int i=0; i<this.recyclerView.getChildCount(); i++){
            VAlarmViewHolder viewHolder = (VAlarmViewHolder) this.recyclerView.getChildViewHolder(this.recyclerView.getChildAt(i));
            viewHolder.updateWithAnimation(300);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.cAlarm = new CAlarm(this.getActivity());
        this.cAlarm.onCreate(Locale.KOREA);

        this.vDashboard = new VDashboard(this.view, this.cAlarm, (v)->this.editAlarm(this.cAlarm.getNextAlarmIndex()), this, this);

        this.vAlarmAdapter = new VAlarmAdapter(this.cAlarm, this,this, this);

        this.recyclerView =  this.view.findViewById(R.id.alarm_list_items);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.vAlarmAdapter);
        new ItemTouchHelper(new OSimpleCallback()).attachToRecyclerView(this.recyclerView);

        this.createAlarmBtn =  this.view.findViewById(R.id.alarm_list_create);
        this.createAlarmBtn.setOnClickListener((v)->createAlarm());
        this.view.getViewTreeObserver().addOnGlobalLayoutListener(new CreateAlarmButtonAlignmentListener());

        this.update();
    }

    /**
     * for save & load create alarm button location
     */
    @Override
    public void onPause() {
        super.onPause();
        this.createAlarmBtn.save();
    }
    public class CreateAlarmButtonAlignmentListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() { // 사용자에게 화면이 보여질 때
            LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
            int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
            View alignmentTarget = recyclerView.getChildAt(lastVisiblePosition);
            if(alignmentTarget==null){
                createAlarmBtn.load((int) (recyclerView.getY()), view.getHeight(), recyclerView.getWidth());
            }else{
                createAlarmBtn.load((int) (recyclerView.getY()+alignmentTarget.getY()+alignmentTarget.getHeight()), view.getHeight(), recyclerView.getWidth());
            }
        }
    }

    /**
     * Navigate to alarm setting fragment
     */
    private void goToAlarmSetting() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MAlarm", this.cAlarm.getCurrentAlarm()); // for set alarm setting items
        bundle.putSerializable("SaveActionListener", new SaveListener()); // for callback
        bundle.putInt("MAlarmCount", this.cAlarm.getMAlarms().size()+1); // for alarm name
        Navigation.findNavController(this.getView()).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
    }

    /**
     * show remove confirm dialog
     */
    private void showRemoveConfirmDialog(int targetPosition) {
        MAlarm target = this.cAlarm.getAlarm(targetPosition);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle("알람 삭제");
        dialog.setMessage(target.getName()+" 알람을 삭제하시겠습니까?");
        dialog.setPositiveButton(this.getContext().getResources().getString(R.string.common_ok), (dialog1, which) -> {
            this.cAlarm.removeAlarm(targetPosition);
            this.vAlarmAdapter.notifyItemRemoved(targetPosition);
            this.cAlarm.store();
            this.vDashboard.update();
            this.update();
        });
        dialog.setNegativeButton(this.getContext().getResources().getString(R.string.common_cancel), null);
        dialog.create().show();
    }
}