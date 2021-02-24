package com.onandon.moca.domain.view.alarm.setting.comp;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.onandon.moca.R;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.technical.device.TEarphone;

public class VEarphone implements OCustomViewCompLifeCycle {

    // Associate
    private Activity activity;
        // View
        private TextView earphoneNotice;

    /**
     * System Callback
     */
    @Override public void onCreate(Activity activity) { this.activity=activity; }
    @Override public void associateAndInitView(View view){ this.earphoneNotice = view.findViewById(R.id.alarm_setting_earphone); }

    /**
     * Update
     */
    public void update(){ this.earphoneNotice.setVisibility(TEarphone.isEarphoneConnected(this.activity)? View.VISIBLE : View.INVISIBLE); }
}
