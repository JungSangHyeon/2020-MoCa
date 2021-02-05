package com.onandon.moca.view.alarm.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.device.TVibrator;
import com.onandon.moca.view.customView.OMovableFloatingActionButton;
import com.onandon.moca.view.customView.OMoveByTouchManager;

import java.io.Serializable;
import java.util.Collections;
import java.util.Locale;

public class VAlarmList extends Fragment implements View.OnLongClickListener, VNextAlarmInfo.VAlarmListUpdateCallback {

    /**
     *  View
     */
    private VNextAlarmInfo vNextAlarmInfo;
    private RecyclerView recyclerView;
    private VAlarmAdapter vAlarmAdapter;
    private OMovableFloatingActionButton createAlarmBtn;

    /**
     *  Control
     */
    private CAlarm cAlarm;

    /**
     * Constructor
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create Components
        this.cAlarm = new CAlarm(this.getActivity());
        this.cAlarm.onCreate(Locale.KOREA);

        // Associate View
        View view = inflater.inflate(R.layout.alarm_list, container, false);

        this.vNextAlarmInfo = new VNextAlarmInfo(view, this.cAlarm, (v)->editNextAlarm(), this);


        this.vAlarmAdapter = new VAlarmAdapter((v)->editAlarm(v), this, this.cAlarm, this.vNextAlarmInfo, this);
        this.recyclerView = view.findViewById(R.id.alarm_list_recycleview);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setAdapter(this.vAlarmAdapter);
        new ItemTouchHelper(new OSimpleCallback()).attachToRecyclerView(this.recyclerView);

        this.createAlarmBtn = view.findViewById(R.id.alarm_list_create);
        this.createAlarmBtn.setOnClickListener((v)->createAlarm());
        new OMoveByTouchManager(this.createAlarmBtn).registerMoveByTouch();

        this.vNextAlarmInfo.update();

        return view;
    }


    @Override
    public void update() {
        this.vNextAlarmInfo.update();
        this.recyclerView.setAdapter(this.vAlarmAdapter);
    }



    /**
     * callback methods for create, save, edit, remove alarm
     */
    private void createAlarm() {
        this.cAlarm.createAlarm();
        this.goToAlarmSetting();
    }
    private void saveAlarm() {
        this.cAlarm.saveAlarm();
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
        this.vNextAlarmInfo.update();
    }
    private void editAlarm(View view) {
        this.cAlarm.editAlarm(this.recyclerView.getChildAdapterPosition(view));
        this.goToAlarmSetting();
    }
    private void editNextAlarm() {
        this.cAlarm.editAlarm(this.cAlarm.getNextAlarmIndex());
        this.goToAlarmSetting();
    }
    private void removeAlarm(int targetPosition){
        this.showRemoveConfirmDialog(targetPosition);
    }

    /**
     * on Pause & on Resume for save & load create alarm button location
     */
    @Override public void onResume() { super.onResume(); this.createAlarmBtn.load(); }
    @Override public void onPause() { super.onPause(); this.createAlarmBtn.save(); }


    @Override
    public boolean onLongClick(View view) {
        // roll back 에 대비해 남겨 놓음
        return true;
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
    private class SaveListener implements View.OnClickListener, Serializable { @Override public void onClick(View view) { saveAlarm(); }}

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
            this.vNextAlarmInfo.update();
        });
        dialog.setNegativeButton(this.getContext().getResources().getString(R.string.common_cancel), null);
        dialog.create().show();
    }

    /**
     * Simple Callback for recycler view
     */
    private class OSimpleCallback extends ItemTouchHelper.SimpleCallback{
        /**
         * Working variable
         */
        private boolean startAction = true, moved = false;
        private RecyclerView.ViewHolder selectedViewHolder;

        // Constructor
        public OSimpleCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(cAlarm.getMAlarms(), fromPosition, toPosition);

            cAlarm.store();
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            this.moved = true;
            return false;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(startAction){
                new TVibrator((Activity)getContext()).start(new int[][]{{100,255}}, -1);
                this.moved = false;
                this.selectedViewHolder=viewHolder;
            }else if(!this.moved) {
                removeAlarm(this.selectedViewHolder.getAdapterPosition());
            }
            startAction=!startAction;
        }

        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) { }
    }
}