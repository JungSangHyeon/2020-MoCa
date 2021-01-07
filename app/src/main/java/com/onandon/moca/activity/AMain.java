package com.onandon.moca.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.onandon.moca.R;
import com.onandon.moca.utility.ChineseCalendarUtil;
import com.onandon.moca.view.VMain;

import java.util.Calendar;

public class AMain extends AppCompatActivity {

    public final static int REQUEST_OVERLAY_PERMISSION = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.checkOverlayPermission(this);
//        if(!Settings.canDrawOverlays(this)){
//            // ask for setting
//            Intent intent = new Intent(
//                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
//        }

        VMain vMain = new VMain(this);
        vMain.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
//            if (!Settings.canDrawOverlays(this)) {
//                // permission not granted...
//            }
//        }
        this.checkOverlayPermission(this);
    }

    public static void checkOverlayPermission(final Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            new Handler().postDelayed(new Thread(){
                @Override
                public void run() {
                    AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                    ad.setTitle("알람을 앱 위에 띄우기 위해 권한이 필요합니다.");
                    ad.setMessage("다음 목록에서 MoCa를 선택하여 권한을 부여해 주세요.");
                    ad.setPositiveButton("확인", (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivityForResult(intent, 0);
                    });
                    ad.setNegativeButton("종료", (dialogInterface, i) -> {
                        activity.finish(); System.exit(0);
                    });
                    ad.show();
                }
            }, 200);
        }
    }

}