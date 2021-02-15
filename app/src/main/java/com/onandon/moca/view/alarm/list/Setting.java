package com.onandon.moca.view.alarm.list;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarmMode;
import com.onandon.moca.model.ModeManager;
import com.onandon.moca.technical.TAlarm;
import com.onandon.moca.view.alarm.setting.VFlash;
import com.onandon.moca.view.alarm.setting.VPower;
import com.onandon.moca.view.alarm.setting.VRingtone;
import com.onandon.moca.view.alarm.setting.VScreen;
import com.onandon.moca.view.alarm.setting.VVibration;

import java.util.Vector;

public class Setting {

    // Associate
    private Context context;

    // Constructor
    public Setting(Context context) {
        this.context=context;
    }

    public void start() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogBuilder.setTitle("모드 설정");
        View dialogView = layoutInflater.inflate(R.layout.setting, null);
        dialogBuilder.setView(dialogView);

        ModeManager modeManager = new ModeManager(this.context);
        Vector<MAlarmMode> mAlarmModes = modeManager.getCloneMAlarmModes();

        Button noSoundButton =  dialogView.findViewById(R.id.setting_nosoundbutton);
        noSoundButton.setText(Constant.EAlarmMode.eNoSound.getModeName());
        noSoundButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eNoSound.ordinal()); });
        Button soundButton =  dialogView.findViewById(R.id.setting_soundbutton);
        soundButton.setText(Constant.EAlarmMode.eSound.getModeName());
        soundButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eSound.ordinal()); });
        Button crazyButton =  dialogView.findViewById(R.id.setting_crazybutton);
        crazyButton.setText(Constant.EAlarmMode.eCrazy.getModeName());
        crazyButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eCrazy.ordinal()); });
        Button userDefinedButton =  dialogView.findViewById(R.id.setting_userdefinedbutton);
        userDefinedButton.setText(Constant.EAlarmMode.eUserDefined.getModeName());
        userDefinedButton.setOnClickListener((v)->{ init(dialogView, mAlarmModes, Constant.EAlarmMode.eUserDefined.ordinal()); });

        dialogBuilder.setPositiveButton(this.context.getResources().getString(R.string.common_ok), (dialog1, which) -> {
            modeManager.setMAlarmModes(mAlarmModes);
            modeManager.save();
            modeManager.load();
        });
        dialogBuilder.setNegativeButton(this.context.getResources().getString(R.string.common_cancel), null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void init(View dialogView, Vector<MAlarmMode> mAlarmModes, int ordinal) {
        TAlarm tAlarm = new TAlarm((Activity) context);
        tAlarm.onCreate(mAlarmModes.get(ordinal));
        new VPower(dialogView, mAlarmModes.get(ordinal), tAlarm);
        new VRingtone(dialogView, mAlarmModes.get(ordinal));
        new VVibration(dialogView, mAlarmModes.get(ordinal));
        new VFlash(dialogView, mAlarmModes.get(ordinal));
        new VScreen(dialogView, mAlarmModes.get(ordinal));
    }
}
