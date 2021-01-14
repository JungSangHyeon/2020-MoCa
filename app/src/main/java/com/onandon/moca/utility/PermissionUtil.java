package com.onandon.moca.utility;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.onandon.moca.R;

public class PermissionUtil {

    public static void checkOverlayPermission(final Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            new Handler().postDelayed(new Thread(){
                @Override
                public void run() {
                    AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                    ad.setTitle(activity.getResources().getString(R.string.overlayPermissionDialogTitle));
                    ad.setMessage(activity.getResources().getString(R.string.overlayPermissionDialogMessage));
                    ad.setPositiveButton(activity.getResources().getString(R.string.common_ok), (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivityForResult(intent, 0);
                    });
                    ad.setNegativeButton(activity.getResources().getString(R.string.common_off), (dialogInterface, i) -> {
                        activity.finish(); System.exit(0);
                    });
                    ad.show();
                }
            }, 200);
        }
    }
}
