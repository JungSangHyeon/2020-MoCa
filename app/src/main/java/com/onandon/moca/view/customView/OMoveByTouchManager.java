package com.onandon.moca.view.customView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class OMoveByTouchManager implements View.OnTouchListener {

    // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private final static float CLICK_DRAG_TOLERANCE = 10;

    // Working Variable
    private float downRawX, downRawY;
    private float dX, dY;

    // Associate
    private View view;

    // Constructor
    public OMoveByTouchManager(View view){
        this.view=view;
    }
    public void registerMoveByTouch(){
        this.view.setOnTouchListener(this);
    }

    /**
     * Get activity of view
     * @param view view to get activity
     * @return activity of view
     */
    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) { return (Activity)context; }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        // Get Activity
        Activity viewActivity = getActivity(view);

        viewActivity.runOnUiThread(() -> {
            Path path = new Path();
            path.moveTo(view.getX(), view.getY());
            path.lineTo(motionEvent.getRawX(), motionEvent.getRawY());
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override public void onAnimationStart(Animator animation) { }
//                @Override public void onAnimationEnd(Animator animation) { interrupt();}
//                @Override public void onAnimationCancel(Animator animation) { }
//                @Override public void onAnimationRepeat(Animator animation) { }
//            });
            animator.setDuration(0);
            animator.start();
        });
        return false;

//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
//
//        int action = motionEvent.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            this.downRawX = motionEvent.getRawX();
//            this.downRawY = motionEvent.getRawY();
//            this.dX = view.getX() - this.downRawX;
//            this.dY = view.getY() - this.downRawY;
//            return true; // Consumed
//        } else if (action == MotionEvent.ACTION_MOVE) {
//            int viewWidth = view.getWidth();
//            int viewHeight = view.getHeight();
//
//            View viewParent = (View)view.getParent();
//            int parentWidth = viewParent.getWidth();
//            int parentHeight = viewParent.getHeight();
//
//            float newX = motionEvent.getRawX() + this.dX;
//            newX = Math.max(layoutParams.leftMargin, newX); // Don't allow the FAB past the left hand side of the parent
//            newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent
//
//            float newY = motionEvent.getRawY() + this.dY;
//            newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
//            newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent
//
//            view.animate().x(newX).y(newY).setDuration(0).start();
//            return true; // Consumed
//        } else if (action == MotionEvent.ACTION_UP) {
//
//            float upRawX = motionEvent.getRawX();
//            float upRawY = motionEvent.getRawY();
//
//            float upDX = upRawX - this.downRawX;
//            float upDY = upRawY - this.downRawY;
//
//            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
//                return view.performClick();
//            } else { // A drag
//                return true; // Consumed
//            }
//        } else {
//            return view.onTouchEvent(motionEvent);
//        }
    }
}