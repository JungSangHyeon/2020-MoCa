package com.onandon.moca.view.customView;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;
import java.util.logging.Handler;

public class OMovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

    private float downRawX, downRawY;
    private float dX, dY;

    public OMovableFloatingActionButton(Context context) { super(context);init(); }
    public OMovableFloatingActionButton(Context context, AttributeSet attrs) { super(context, attrs);init(); }
    public OMovableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr);init(); }


    private void init() {
        setOnTouchListener(this);
    }


    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;
            return true; // Consumed
        } else if (action == MotionEvent.ACTION_MOVE) {

            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();

            View viewParent = (View)view.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            float newX = motionEvent.getRawX() + dX;
            newX = Math.max(layoutParams.leftMargin, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + dY;
            newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent

            view.animate().x(newX).y(newY).setDuration(0).start();
            return true; // Consumed
        } else if (action == MotionEvent.ACTION_UP) {

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                return performClick();
            } else { // A drag
                return true; // Consumed
            }
        } else {
            return super.onTouchEvent(motionEvent);
        }
    }

    public void save() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("OMovableFloatingActionButton", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("x", this.getX()-this.getLeft());
        editor.putFloat("y", this.getY()-this.getTop());
        editor.commit();
    }

    public void load() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("OMovableFloatingActionButton", Context.MODE_PRIVATE);
        float x = prefs.getFloat("x", -1);
        float y = prefs.getFloat("y", -1);
        if(x!=-1 && y!=-1){
            this.setX(x);
            this.setY(y);
        }
    }

    public void startAutoMove() {

        new Thread(){
            public void run(){
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                int minimumX = 0, maximumX = width;
                int minimumY = 0, maximumY = height;
                int minimumMoveTime = 100, maximumMoveTime = 1000;
                int minimumWaitTime = 1000, maximumWaitTime = 3000;

                Random random = new Random();
                while(true){
                    int randomX = random.nextInt(maximumX - minimumX + 1) + minimumX;
                    int randomY = random.nextInt(maximumY - minimumY + 1) + minimumY;
                    int randomMoveTime = random.nextInt(maximumMoveTime - minimumMoveTime + 1) + minimumMoveTime;
                    int randomWaitTime = random.nextInt(maximumWaitTime - minimumWaitTime + 1) + minimumWaitTime;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Path path = new Path();
                            path.moveTo(getX(), getY());
                            path.lineTo(randomX, randomY);
                            ObjectAnimator animator = ObjectAnimator.ofFloat(OMovableFloatingActionButton.this, View.X, View.Y, path);
                            animator.setDuration(randomMoveTime);
                            animator.start();
                        }
                    });

                    try {
                        Thread.sleep(randomWaitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}