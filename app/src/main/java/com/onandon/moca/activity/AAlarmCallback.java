package com.onandon.moca.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.utility.ShowOnLockScreenUtil;
import com.onandon.moca.view.alarm.callback.VAlarmCallBack;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AAlarmCallback extends AppCompatActivity {

    // Component
    private VAlarmCallBack vCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmcallback);

        this.vCallBack = new VAlarmCallBack(this);
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            MAlarm mAlarm = (MAlarm) bundle.getSerializable(MAlarm.class.getSimpleName());
            ShowOnLockScreenUtil.makeActivityAbleToShowOnLockedScreen(this);
            this.vCallBack.onCreate(mAlarm);
        }
    }

    @Override protected void onDestroy() { super.onDestroy();this.vCallBack.onDestroy(); }
}



