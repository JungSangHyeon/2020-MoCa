package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.TextView;

import com.onandon.moca.R;
import com.onandon.moca.technical.device.TEarphone;

public class VEarphone {

    // Associate
    private TextView earphoneNotice;

    // Constructor
    public VEarphone(View view) {
        this.earphoneNotice = view.findViewById(R.id.alarm_setting_earphone);
        this.earphoneNotice.setVisibility(TEarphone.isEarphoneConnected(view.getContext())? View.VISIBLE : View.INVISIBLE);
    }
}
