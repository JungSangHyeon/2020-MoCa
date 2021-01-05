package com.onandon.moca.view.alarm;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.view.alarm.list.VAlarmList;
import com.onandon.moca.view.alarm.setting.VAlarmSetting;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VAlarm extends Fragment implements View.OnClickListener {

    private final AppCompatActivity mainActivity;
    private CAlarm cAlarm;
    private Map<Integer, Fragment> fragmentMap; // <R.Id, index>

    public VAlarm(AppCompatActivity mainActivity) {
        Log.d("VAlarm", "VAlarm");
        this.mainActivity = mainActivity;
        this.cAlarm = new CAlarm(mainActivity);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Log.d("VAlarmFragment", "onCreateView");
        View view = inflater.inflate(R.layout.alarm, container, false);
        this.cAlarm.onCreate(Locale.KOREA);

        this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.alarm, new VAlarmList(this, this.cAlarm))
                .commit();

        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d("VFragmentAlarmMain", "onClick");

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDestroyView() {
        Log.d("VAlarm", "onDestroyView");
        String toastText = String.format("VAlarm onDestroyView");
        Toast.makeText(this.mainActivity, toastText, Toast.LENGTH_LONG).show();

        super.onDestroyView();
        this.cAlarm.onDestroy();
    }
}
