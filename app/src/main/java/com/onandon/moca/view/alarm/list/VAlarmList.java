package com.onandon.moca.view.alarm.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;

public class VAlarmList extends Fragment implements View.OnClickListener {

    // Associate
    private CAlarm cAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.cAlarm = (CAlarm) this.getArguments().getSerializable("CAlarm");

        View view = inflater.inflate(R.layout.alarm_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.alarm_list_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new VAlarmAdapter(this, this.cAlarm));

        Button createAlarmBtn = view.findViewById(R.id.alarm_list_create);
        createAlarmBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()  == R.id.alarm_list_create) this.cAlarm.createAlarm();
        Bundle bundle = new Bundle();
        bundle.putSerializable("CAlarm", this.cAlarm);
        Navigation.findNavController(view).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
    }
 }
