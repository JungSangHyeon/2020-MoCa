package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.EditText;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VName {

    // Association
    private EditText editTextName;

    // Constructor
    public VName(View view, MAlarm mAlarm) {
        this.editTextName = view.findViewById(R.id.alarm_setting_name);
        this.editTextName.setText(mAlarm.getName());
    }

    public String getName() { return this.editTextName.getText().toString(); }
}
