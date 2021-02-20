package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;

public class VName {

    // Associate
        // View
        private EditText editTextName;

    /**
     * System Callback
     */
    public void onCreate(FragmentActivity activity) { }
    public void onViewCreated(View view){ this.editTextName = view.findViewById(R.id.alarm_setting_name); }

    /**
     * Update
     */
    public void update(MAlarm mAlarm){ this.editTextName.setText(mAlarm.getName()); }

    /**
     * Getter
     */
    public String getName() { return this.editTextName.getText().toString(); }
}
