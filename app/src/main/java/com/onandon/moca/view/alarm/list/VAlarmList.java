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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;

public class VAlarmList extends Fragment implements View.OnClickListener {

    private final View.OnClickListener vAlarm;
    private final CAlarm cAlarm;

    public VAlarmList(View.OnClickListener vAlarm, CAlarm cAlarm) {
        super(R.layout.alarm_list);
        Log.d("VAlarmListFragment", "VAlarmListFragment");
        this.vAlarm = vAlarm;
        this.cAlarm = cAlarm;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Log.d("VAlarmListFragment", "onCreateView");
        View view = inflater.inflate(R.layout.alarm_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.alarm_list_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new VAlarmAdapter(this.vAlarm, this.cAlarm));

        Button createAlarmBtn = view.findViewById(R.id.alarm_list_create);
        createAlarmBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d("VFragmentAlarmMain", "onClick");
        if (view.getId()  == R.id.alarm_list_create) {
            this.cAlarm.createAlarm();
        }
        this.vAlarm.onClick(view);
    }
 }
