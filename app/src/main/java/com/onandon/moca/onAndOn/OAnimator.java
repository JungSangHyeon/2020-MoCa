package com.onandon.moca.onAndOn;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.Vector;

public class OAnimator {

    public static void animateAlphaChange(int duration, float alpha, Animator.AnimatorListener listener, View...targets){
        OAnimator.animate(duration, "alpha", alpha, listener, targets);
    }

    private static void animate(int duration, String animationTarget, float toValue, Animator.AnimatorListener listener, View...targets){
        Vector<Animator> animatorVector = new Vector<>();
        for(View target : targets){
            ObjectAnimator animator = ObjectAnimator.ofFloat(target, animationTarget, toValue);
            animator.setDuration(duration);
            animatorVector.add(animator);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorVector);
        animatorSet.setDuration(duration);
        if(listener!=null){ animatorSet.addListener(listener); }
        animatorSet.start();
    }
}
