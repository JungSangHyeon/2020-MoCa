package com.onandon.moca.view.alarm.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.view.customView.OMovableFloatingActionButton;

import java.io.Serializable;
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

        this.createAlarmBtn = view.findViewById(R.id.alarm_list_create);
        this.createAlarmBtn.setOnClickListener(this.actionListener);

        return view;
    }

    @Override
    public boolean onLongClick(View view) {
        this.removeAlarm(view);
        return true;
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
        this.createAlarmBtn.startAutoMove();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.createAlarmBtn.save();
    }
}