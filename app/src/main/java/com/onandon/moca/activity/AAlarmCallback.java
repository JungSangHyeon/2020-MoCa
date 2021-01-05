package com.onandon.moca.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.alarm.VAlarmCallBack;

public class AAlarmCallback extends AppCompatActivity {

    private VAlarmCallBack vCallBack;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmcallback);

        Log.d("AAlarmCallback", "onCreate");
        String toastText = "AAlarmCallback::onCreate";
        Toast.makeText(this.getApplicationContext(), toastText, Toast.LENGTH_LONG).show();

        this.vCallBack = new VAlarmCallBack(this);
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            MAlarm mAlarm = (MAlarm) bundle.getSerializable(MAlarm.class.getSimpleName());

            this.makeActivityAbleToShowOnLockedScreen();
            this.vCallBack.onCreate(mAlarm);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("AAlarmCallback", "onDestroy");
        String toastText = "AAlarmCallback::onDestroy";
        Toast.makeText(this.getApplicationContext(), toastText, Toast.LENGTH_LONG).show();

        this.vCallBack.onDestroy();
    }

    public  void makeActivityAbleToShowOnLockedScreen(){ // Show Activity On Locked Screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setShowWhenLocked(true);
            this.setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
            if(keyguardManager!=null){ keyguardManager.requestDismissKeyguard(this, null);}
        } else {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }
}



