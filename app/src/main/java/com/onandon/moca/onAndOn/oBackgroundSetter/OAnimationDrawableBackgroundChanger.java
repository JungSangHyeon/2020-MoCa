package com.onandon.moca.onAndOn.oBackgroundSetter;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class OAnimationDrawableBackgroundChanger {

    public static void changeBackground(View target, Drawable background) {
        AnimationDrawable nowBackground = (AnimationDrawable) target.getBackground();
        nowBackground.stop();
        nowBackground.selectDrawable(0);

        AnimationDrawable newBackground = (AnimationDrawable) background;
        target.setBackground(newBackground);
        newBackground.start();
    }
}
