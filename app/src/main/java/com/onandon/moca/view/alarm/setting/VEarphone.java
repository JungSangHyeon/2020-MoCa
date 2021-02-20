package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.technical.device.TEarphone;

public class VEarphone {

    // Associate
    private FragmentActivity activity;
        // View
        private TextView earphoneNotice;

    /**
     * System Callback
     */
    public void onCreate(FragmentActivity activity) { this.activity=activity; }
    public void onViewCreated(View view){ this.earphoneNotice = view.findViewById(R.id.alarm_setting_earphone); }

    /**
     * Update
     */
    public void update(MAlarm mAlarm){ this.earphoneNotice.setVisibility(TEarphone.isEarphoneConnected(this.activity)? View.VISIBLE : View.INVISIBLE); }
}
