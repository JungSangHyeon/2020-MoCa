package com.onandon.moca.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onandon.moca.R;
import com.onandon.moca.view.VMain;

public class AMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(Settings.canDrawOverlays(this)){
            VMain vMain = new VMain(this);
            vMain.onCreate();
        }else{
            this.startManageOverlayPermissionActivity();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Settings.canDrawOverlays(this)){
            VMain vMain = new VMain(this);
            vMain.onCreate();
        }else{
            this.finish();
        }
    }

    private void startManageOverlayPermissionActivity() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivityForResult(intent, 0);
    }
}