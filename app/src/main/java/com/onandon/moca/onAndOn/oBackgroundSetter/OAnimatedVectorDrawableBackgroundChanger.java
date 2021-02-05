package com.onandon.moca.onAndOn.oBackgroundSetter;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class OAnimatedVectorDrawableBackgroundChanger {

    public static void changeBackground(View target, Drawable background) {
        AnimatedVectorDrawable newBackground = (AnimatedVectorDrawable) background;
        target.setBackground(newBackground);
        newBackground.start();
    }
}
