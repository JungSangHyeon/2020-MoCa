package com.onandon.moca.domain.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.onandon.moca.R;
import com.onandon.moca.onAndOnAsset.utility.UShowOnLockScreen;
import com.onandon.moca.onAndOnAsset.view.comp.activity.OFullScreenActivity;

public class AAlarmCallback extends OFullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmcallback);

        UShowOnLockScreen.makeActivityAbleToShowOnLockedScreen(this);
    }
}



