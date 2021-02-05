package com.onandon.moca.onAndOn;

import android.animation.ObjectAnimator;
import android.view.View;

public class OAnimator {
    public static void animateEnableChange(View target){
        ObjectAnimator animation;
        if(target.isEnabled()) { animation = ObjectAnimator.ofFloat(target, "alpha", 1); }
        else { animation = ObjectAnimator.ofFloat(target, "alpha", 0.3f); }
        animation.setDuration(300);
        animation.start();
    }
}
