package com.onandon.moca.domain.view.alarm.setting.comp;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;

public class VName implements OCustomViewCompLifeCycle {

    // Associate
        // View
        private EditText editTextName;

    /**
     * System Callback
     */
    @Override public void onCreate(Activity activity) { }
    @Override public void associateAndInitView(View view){ this.editTextName = view.findViewById(R.id.alarm_setting_name); }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData){ this.editTextName.setText(mAlarmData.getName()); }

    /**
     * Getter
     */
    public String getName() { return this.editTextName.getText().toString(); }
}
