package com.onandon.moca.view.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.view.alarm.list.VAlarmList;
import com.onandon.moca.view.alarm.setting.VAlarmSetting;

import java.util.Locale;

public class VAlarm extends Fragment implements View.OnClickListener {

    // Component
    private CAlarm cAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create Component
        this.cAlarm = new CAlarm(this.getActivity());
        this.cAlarm.onCreate(Locale.KOREA);

        View view = inflater.inflate(R.layout.alarm, container, false);
        this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.alarm, new VAlarmList(this, this.cAlarm))
                .commit();
        return view;
    }

    @Override
    public void onClick(View view) {
       Fragment newFragment = null;
       if (view.getId() == R.id.alarm_setting_save || view.getId() == R.id.alarm_setting_cancel ) {
           newFragment = new VAlarmList(this, this.cAlarm);
       } else if (view.getId() == R.id.alarm_list_create || view.getId() == R.id.alarm_list_item ) {
           newFragment = new VAlarmSetting(this, this.cAlarm);
       }
        this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.alarm, newFragment)
                .commit();
     }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.cAlarm.onDestroy();
    }
}
