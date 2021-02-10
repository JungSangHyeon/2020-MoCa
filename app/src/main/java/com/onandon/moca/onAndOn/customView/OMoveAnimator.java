package com.onandon.moca.onAndOn.customView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.onandon.moca.R;

import java.util.Random;

/**
 * <p>
 *      A move animator. Move view to random location on screen.
 * </p>
 * @version 1.0.0
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OMoveAnimator {

    // Attribute
    private int minimumMoveTime = 50, maximumMoveTime = 100, minimumWaitTime = 100, maximumWaitTime = 100;

    // Working Variable
    private boolean running = false;

    // Associate
    private View view;

    // Constructor
    public OMoveAnimator(View view){ this.view=view; }

    /**
     * if view size initialized, start move animation.
     * else if view size not initialized, add OnLayoutChangeListener and start move animation when listener called
     */
    public void registerMoveAnimation(){
        this.running = true;
        if(this.view.getWidth()==0 && this.view.getHeight()==0){
            // if view size not initialized, add listener and start move when listener called
            view.addOnLayoutChangeListener((view, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {
                this.startAutoMove();
            });
        }else{
            this.startAutoMove();
        }
    }
    /**
     * if move animation running, stop animation.
     */
    public void stopMoveAnimation(){
        if(this.running){ this.running = false; }
    }

    private void startAutoMove() {
        Thread animationThread = new Thread(){
            public void run(){
                // Get Activity
                Activity viewActivity = getActivity(view);

                // Get Screen Size
                Point size = getScreenSize(viewActivity);
                int screenWidth = size.x;
                int screenHeight = size.y;

                // Get Action Bar Size
                int actionBarHeight = getActionBarHeight(viewActivity);

                // Set Random Min, Max Value
                View parent = (View) view.getParent();
                int minimumX = (int) parent.getX(), maximumX = minimumX+ parent.getWidth()-view.getWidth();
                int minimumY = (int) parent.getY()+actionBarHeight, maximumY = minimumY + parent.getHeight()-view.getHeight()-actionBarHeight;

                while(running){
                    // Get Random Value
                    int randomX = getRandomValueBetween(minimumX, maximumX);
                    int randomY = getRandomValueBetween(minimumY, maximumY);

                    int randomMoveTime = getRandomValueBetween(minimumMoveTime, maximumMoveTime);
                    int randomWaitTime = getRandomValueBetween(minimumWaitTime, maximumWaitTime);

                    // Move View
                    viewActivity.runOnUiThread(() -> {
                        Path path = new Path();
                        path.moveTo(view.getX(), view.getY());
                        path.lineTo(randomX, randomY);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override public void onAnimationStart(Animator animation) { }
                            @Override public void onAnimationEnd(Animator animation) { interrupt();}
                            @Override public void onAnimationCancel(Animator animation) { }
                            @Override public void onAnimationRepeat(Animator animation) { }
                        });
                        animator.setDuration(randomMoveTime);
                        animator.start();
                    });

                    // Wait Until Animation End
                    synchronized(this){
                        try { this.wait(); } catch (InterruptedException e) { /*Do Nothing*/ }
                    }

                    // Sleep As Long As Wait Time
                    try { this.sleep(randomWaitTime); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        };
        animationThread.start();
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

    /**
     * Get screen size
     * @param activity activity to get screen size
     * @return screen size (size.x=width, size.y=height)
     */
    private Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Get action bar height
     * @param activity activity to get action bar height
     * @return action bar height
     */
    private int getActionBarHeight(Activity activity) {
        TypedValue typedValue = new TypedValue();
        int actionBarHeight = 0;
        if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * get random int between {@param min}, {@param max} (min<= return && return <= max)
     * @param min min value of return value
     * @param max max value of return value
     * @return random int between {@param min}, {@param max}
     */
    private int getRandomValueBetween(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
