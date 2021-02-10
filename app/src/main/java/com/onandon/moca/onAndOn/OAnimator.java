package com.onandon.moca.onAndOn;

import android.animation.ObjectAnimator;
import android.view.View;

public class OAnimator {
    public static ObjectAnimator animateEnableChange(View target, int duration){
        ObjectAnimator animation;
        if(target.isEnabled()) { animation = ObjectAnimator.ofFloat(target, "alpha", 1); }
        else { animation = ObjectAnimator.ofFloat(target, "alpha", 0.3f); }
        animation.setDuration(duration);
        animation.start();
        return animation;
    }
    public static ObjectAnimator animateAlphaChange(View target, int duration, float alpha){
        ObjectAnimator animation = ObjectAnimator.ofFloat(target, "alpha", alpha);
        animation.setDuration(duration);
        animation.start();
        return animation;
    }

    public static ObjectAnimator animateVisibleToGone(View target){
        ObjectAnimator animation = ObjectAnimator.ofFloat(target, "alpha", 0f);
        animation.setDuration(300);
        animation.start();
        return animation;
    }
    public static ObjectAnimator animateGoneToVisible(View target){
        ObjectAnimator animation = ObjectAnimator.ofFloat(target, "alpha", 1f);
        animation.setDuration(300);
        animation.start();
        return animation;
    }
}
