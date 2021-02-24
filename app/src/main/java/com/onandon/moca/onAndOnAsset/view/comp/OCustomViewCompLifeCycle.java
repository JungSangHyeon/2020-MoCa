package com.onandon.moca.onAndOnAsset.view.comp;

import android.app.Activity;
import android.view.View;

import com.onandon.moca.domain.view.alarm.setting.VAlarmSetting;

public interface OCustomViewCompLifeCycle {
    void onCreate(Activity activity);
    void associateAndInitView(View view);
}
