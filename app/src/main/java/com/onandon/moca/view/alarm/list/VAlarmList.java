package com.onandon.moca.view.alarm.list;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.device.TVibrator;
import com.onandon.moca.view.customView.OMovableFloatingActionButton;
import com.onandon.moca.view.customView.OMoveAnimator;
import com.onandon.moca.view.customView.OMoveByTouchManager;

import java.io.Serializable;
import java.util.Collections;
import java.util.Locale;

public class VAlarmList extends Fragment implements View.OnLongClickListener {

    // Associate
    private RecyclerView recyclerView;

    // Component
    private CAlarm cAlarm;
    private ActionListener actionListener;
    private VAlarmAdapter vAlarmAdapter;
    private VNextAlarmInfo vNextAlarmInfo;
    private OMovableFloatingActionButton createAlarmBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.cAlarm = new CAlarm(this.getActivity());
        this.cAlarm.onCreate(Locale.KOREA);
        this.actionListener = new ActionListener();

        View view = inflater.inflate(R.layout.alarm_list, container, false);

        this.vNextAlarmInfo = new VNextAlarmInfo(view, this.cAlarm);
        this.vNextAlarmInfo.update();

        this.vAlarmAdapter = new VAlarmAdapter(this.actionListener, this, this.cAlarm, this.vNextAlarmInfo);

        this.recyclerView = view.findViewById(R.id.alarm_list_recycleview);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.recyclerView.setAdapter(this.vAlarmAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
               0) {
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
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }
            private boolean startAction = true, moved = false;
            private RecyclerView.ViewHolder selectedViewHolder;
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(startAction){
                    new TVibrator((Activity)getContext()).start(new int[][]{{100,255}}, -1);
                    this.moved = false;
                    this.selectedViewHolder=viewHolder;
                }else if(!this.moved) {
                    showRemoveConfirmDialog(this.selectedViewHolder.getAdapterPosition());
                }
                startAction=!startAction;
            }
        } ;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);

        this.createAlarmBtn = view.findViewById(R.id.alarm_list_create);
        this.createAlarmBtn.setOnClickListener(this.actionListener);

        return view;
    }

    @Override
    public boolean onLongClick(View view) {
        // roll back 에 대비해 남겨 놓음
        return true;
    }

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

    private class ActionListener implements View.OnClickListener, Serializable {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.alarm_list_create) {
                createAlarm();
            } else if (view.getId() == R.id.alarm_setting_save) {
                saveAlarm();
            } else {
                editAlarm(view);
            }
        }
    }

    private void createAlarm() {
        this.cAlarm.createAlarm();
        this.goToAlarmSetting();
    }
    private void editAlarm(View view) {
        this.cAlarm.editAlarm(this.recyclerView.getChildAdapterPosition(view));
        this.goToAlarmSetting();
    }
    private void removeAlarm(View view) {
        this.cAlarm.removeAlarm(this.recyclerView.getChildAdapterPosition(view));
        this.cAlarm.store();
        this.vAlarmAdapter.notifyItemRemoved(this.recyclerView.getChildAdapterPosition(view));
        this.vNextAlarmInfo.update();
    }
    private void saveAlarm() {
        this.cAlarm.saveAlarm();
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
        this.vNextAlarmInfo.update();
    }

    private void goToAlarmSetting() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MAlarm", this.cAlarm.getAlarm());
        bundle.putSerializable("SaveActionListener", this.actionListener);
        Navigation.findNavController(this.getView()).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.createAlarmBtn.load();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.createAlarmBtn.save();
    }
}