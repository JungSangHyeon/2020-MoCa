package com.onandon.moca.utility;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class PermissionUtil {

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
