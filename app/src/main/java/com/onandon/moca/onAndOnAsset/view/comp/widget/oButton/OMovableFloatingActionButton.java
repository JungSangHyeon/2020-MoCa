package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OMovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener, View.OnLongClickListener {

    // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private final static float CLICK_DRAG_TOLERANCE = 10;
    private final static String  X = "x";
    private final static String  Y = "y";
    private final static String  UserMoved = "userMoved";

    // Working Variable
    private float defaultX, defaultY;
    private float downRawX, downRawY;
    private float dX, dY;
    private boolean userMoved = false, moved = false, reseted = false, pressed = false;

    // Constructor
    public OMovableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        this.setOnLongClickListener(this);
    }

    public void save() {
        SharedPreferences prefs = this.getContext().getSharedPreferences(OMovableFloatingActionButton.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("x", this.getX());
        editor.putFloat("y", this.getY());
        editor.putBoolean("userMoved", this.userMoved);
        editor.commit();
    }
    public void load(int limitTop, int parentHeight, int width) {
        this.defaultX = width - this.getWidth() - 60;
        this.defaultY = (parentHeight-limitTop<this.getHeight()+30)? parentHeight - this.getHeight() - 30:limitTop+30;

        SharedPreferences prefs = this.getContext().getSharedPreferences(OMovableFloatingActionButton.class.getSimpleName(), Context.MODE_PRIVATE);
        float x = prefs.getFloat(X, -1);
        float y = prefs.getFloat(Y, -1);
        boolean userMoved = prefs.getBoolean(UserMoved, false);
        this.userMoved=userMoved;

        this.setX((this.userMoved && x!=-1)? x:this.defaultX);
        this.setY((this.userMoved && y!=-1)? y:this.defaultY);
    }

    @Override
    public boolean onLongClick(View v) {
        // ????????? ????????? ?????? ?????? ??????, ????????? ????????? ???????????? ?????? ????????? ??? ????????? ????????????, ????????? ?????? ?????????
        if(this.userMoved && this.pressed && !this.moved ){
            this.userMoved = false;
            this.reseted = true;
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(X, this.defaultX);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(Y, this.defaultY);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY);
            animator.setDuration(300);
            animator.start();
            this.save();
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            this.reseted =false;
            this.pressed=true;

            this.downRawX = motionEvent.getRawX();
            this.downRawY = motionEvent.getRawY();
            this.dX = view.getX() - this.downRawX;
            this.dY = view.getY() - this.downRawY;
            return false; // Consumed
        } else if (action == MotionEvent.ACTION_MOVE && ! this.reseted) { // ???????????? ??? ??? ????????? ????????? ??? ??????

            float moveRawX = motionEvent.getRawX();
            float moveRawY = motionEvent.getRawY();

            float moveDX = moveRawX - this.downRawX;
            float moveDY = moveRawY - this.downRawY;

            if (Math.abs(moveDX) > CLICK_DRAG_TOLERANCE || Math.abs(moveDY) > CLICK_DRAG_TOLERANCE) {
                this.moved =true; // ?????? ?????? ?????? ???????????? ????????? ????????? ??????
            }

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
            return true; // Consumed
        } else if (action == MotionEvent.ACTION_UP) {
            this.moved =false;
            this.pressed=false;

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - this.downRawX;
            float upDY = upRawY - this.downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                if(!this.reseted){
                    return view.performClick();
                }else{
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