package com.onandon.moca.view.customView;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OMovableFloatingActionButtonTEMP extends FloatingActionButton implements View.OnTouchListener, View.OnLongClickListener {

    private boolean userMoved = false, moving = false, longClicked = false;
    private float defaultX, defaultY;

    public OMovableFloatingActionButtonTEMP(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        this.setOnLongClickListener(this);
    }

    public void save() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("OMovableFloatingActionButton", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("x", this.getX());
        editor.putFloat("y", this.getY());
        editor.putBoolean("userMoved", this.userMoved);
        editor.commit();
    }
    public void load(int limitHeight, int parentHeight) {
        defaultX = this.getX();
        defaultY = (parentHeight-limitHeight<this.getHeight()+30)? parentHeight - this.getHeight() - 30:limitHeight+30;

        SharedPreferences prefs = this.getContext().getSharedPreferences("OMovableFloatingActionButton", Context.MODE_PRIVATE);
        float x = prefs.getFloat("x", -1);
        float y = prefs.getFloat("y", -1);
        boolean userMoved = prefs.getBoolean("userMoved", false);
        this.userMoved=userMoved;
        if(this.userMoved){
            if(x!=-1 && y!=-1){
                this.setX(x);
                this.setY(y);
            }
        }else{
            if(parentHeight-limitHeight<this.getHeight()+30){
                this.setY(parentHeight - this.getHeight() - 30);
            }else{
                this.setY(limitHeight+30);
            }
        }
    }

    // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private final static float CLICK_DRAG_TOLERANCE = 10;

    // Working Variable
    private float downRawX, downRawY;
    private float dX, dY;

    @Override
    public boolean onLongClick(View v) {
        if(this.userMoved && !moving){
            this.userMoved = false;
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", this.defaultX);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", this.defaultY);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY);
            animator.setDuration(300);
            animator.start();
            longClicked = true;
            this.save();
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            this.downRawX = motionEvent.getRawX();
            this.downRawY = motionEvent.getRawY();
            this.dX = view.getX() - this.downRawX;
            this.dY = view.getY() - this.downRawY;
            return false; // Consumed
        } else if (action == MotionEvent.ACTION_MOVE) {
            moving=true;
            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();

            View viewParent = (View)view.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            float newX = motionEvent.getRawX() + this.dX;
            newX = Math.max(layoutParams.leftMargin, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + this.dY;
            newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent

            view.animate().x(newX).y(newY).setDuration(0).start();
            return false; // Consumed
        } else if (action == MotionEvent.ACTION_UP) {
            moving=false;

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - this.downRawX;
            float upDY = upRawY - this.downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                if(!longClicked){
                    return view.performClick();
                }else{
                    longClicked=false;
                    return true;
                }
            } else { // A drag
                this.userMoved = true;
                return true; // Consumed
            }
        } else {
            return view.onTouchEvent(motionEvent);
        }
    }
}