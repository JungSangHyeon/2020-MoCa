package com.onandon.moca.view.alarm.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VName {

    private MAlarm mAlarm;
    private EditText editTextName;
    // Associations
    public VName(View view, MAlarm mAlarm) {
        super();
        Log.println(Log.DEBUG, "VFlash", "");

        this.mAlarm = mAlarm;
        this.editTextName = view.findViewById(R.id.alarm_setting_name);
        this.editTextName.setText(mAlarm.getName());
    }

    public String getName() { return this.editTextName.getText().toString(); }
}
