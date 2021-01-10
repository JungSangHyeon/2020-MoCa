package com.onandon.moca.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.onandon.moca.R;
import com.onandon.moca.utility.PermissionUtil;
import com.onandon.moca.view.VMain;

public class AMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PermissionUtil.checkOverlayPermission(this);

        VMain vMain = new VMain(this);
        vMain.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.checkOverlayPermission(this);
    }
}