package com.onandon.moca.view.customView;


import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class QuickReturnFooterBehavior extends CoordinatorLayout.Behavior<View> {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final long ANIMATION_DURATION = 200;
    private int dyDirectionSum;
    private boolean isShowing;
    private boolean isHiding;

    public QuickReturnFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0 && dyDirectionSum < 0 || dy < 0 && dyDirectionSum > 0) {  // 스크롤이 반대방향으로 전환
            child.animate().cancel();
            dyDirectionSum = 0;
        }
        dyDirectionSum += dy;
        if (dyDirectionSum > child.getHeight()) hideView(child);
        else if (dyDirectionSum < -child.getHeight()) showView(child);
    }

    private void hideView(final View view) {
        if (isHiding || view.getVisibility() != View.VISIBLE) return;
        ViewPropertyAnimator animator = view.animate().translationY(view.getHeight()).setInterpolator(INTERPOLATOR).setDuration(ANIMATION_DURATION);
        animator.setListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { isHiding = true; }
            @Override public void onAnimationEnd(Animator animator) { isHiding = false;view.setVisibility(View.INVISIBLE); }
            @Override public void onAnimationCancel(Animator animator) { isHiding = false;showView(view); }  // 취소되면 다시 보여줌
            @Override public void onAnimationRepeat(Animator animator) { /* no-op*/}
        });
        animator.start();
    }

    private void showView(final View view) {
        if (isShowing || view.getVisibility() == View.VISIBLE) return;
        ViewPropertyAnimator animator = view.animate().translationY(0).setInterpolator(INTERPOLATOR).setDuration(ANIMATION_DURATION);
        animator.setListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { isShowing = true;view.setVisibility(View.VISIBLE); }
            @Override public void onAnimationEnd(Animator animator) { isShowing = false; }
            @Override public void onAnimationCancel(Animator animator) { isShowing = false;hideView(view); } // 취소되면 다시 숨김
            @Override public void onAnimationRepeat(Animator animator) { /* no-op*/ }
        });
        animator.start();
    }
}
