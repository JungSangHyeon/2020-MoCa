package com.onandon.moca.view.customView;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.onandon.moca.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OToggleButton extends CompoundButton implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private AnimatedVectorDrawable defaultStateBackground, checkedStateBackground,
            defaultToCheckedBackground, checkedToDefaultBackground, disabledBackground,
            defaultPressedBackground, checkedPressedBackground;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnClickListener onClickListener;

    // Constructor
    public OToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OToggleButton, 0, 0);
        this.defaultStateBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_default_state_background);
        this.checkedStateBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_checked_state_background);
        this.defaultToCheckedBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_default_to_checked_background);
        this.checkedToDefaultBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_checked_to_default_background);
        this.disabledBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_disabled_background);
        this.defaultPressedBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_default_pressed_background);
        this.checkedPressedBackground = this.getDrawable(context, array, R.styleable.OToggleButton_togglebutton_checked_pressed_background);

        this.defaultToCheckedBackground.registerAnimationCallback(
                new Animatable2.AnimationCallback() {
                    public void onAnimationEnd(Drawable drawable) {
                        setBackground(checkedStateBackground);
                        checkedStateBackground.start();
                    }
                }
        );
        this.checkedToDefaultBackground.registerAnimationCallback(
                new Animatable2.AnimationCallback() {
                    public void onAnimationEnd(Drawable drawable) {
                        setBackground(defaultStateBackground);
                        defaultStateBackground.start();
                    }
                }
        );

        super.setOnCheckedChangeListener(this);
        super.setOnClickListener(this);
        this.setBackground(this.defaultStateBackground);
        this.defaultStateBackground.start();


//        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "translationX", 10f);
//        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.animator.vibrate);
//        animator.setRepeatCount(ObjectAnimator.INFINITE);
//        animator.setDuration(1000);
//        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.isEnabled()){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    this.setBackground(this.isChecked()? this.checkedPressedBackground : this.defaultPressedBackground);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    this.setBackground(this.isChecked()? this.checkedStateBackground : this.defaultStateBackground);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        this.changeBackground();
        if(this.onClickListener !=null){
            this.onClickListener.onClick(v);
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.changeBackground();
        if(this.onCheckedChangeListener !=null){
            this.onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        }
    }

    private void changeBackground() {
        AnimatedVectorDrawable nowBackground = this.isChecked()? this.defaultToCheckedBackground : this.checkedToDefaultBackground;
        this.setBackground(nowBackground);
        nowBackground.start();
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) { this.onCheckedChangeListener =listener; }
    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) { this.onClickListener = listener; }

    private AnimatedVectorDrawable getDrawable(Context context, TypedArray array, int id) {
        return (AnimatedVectorDrawable) context.getDrawable(array.getResourceId(id, -1));
    }

    @Override
    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        if(this.isEnabled()){ this.setBackground(this.defaultStateBackground); this.defaultStateBackground.start();} // disable -> enable
        else{ this.setBackground(this.disabledBackground); } // enable -> disable
    }
}
