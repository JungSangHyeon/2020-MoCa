package com.onandon.moca.onAndOn.oButton.oActionButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.onandon.moca.R;
import com.onandon.moca.onAndOn.OAnimator;
import com.onandon.moca.onAndOn.OConstants;

public abstract class OActionButton extends androidx.appcompat.widget.AppCompatButton implements View.OnTouchListener {

    // Attribute
    protected Drawable normalToPressedBackground, pressedToNormalBackground;

    // Associate
    private OnTouchListener onTouchListener;
    private TypedArray attributeArray;

    // Constructor
    public OActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // get attribute array
        this.attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OButton, 0, 0);

        // set attribute
        this.normalToPressedBackground = this.getBackgroundById(R.styleable.OButton_obutton_normal_to_checked);
        this.pressedToNormalBackground = this.getBackgroundById(R.styleable.OButton_obutton_checked_to_normal);

        // set listener for change background
        super.setOnTouchListener(this);

        // set default background
        this.changeBackground(this.pressedToNormalBackground);
        this.stopNowAnimation();
    }

    @Override
    public void setOnTouchListener(@Nullable OnTouchListener listener) { this.onTouchListener = listener; }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(this.isEnabled()){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN: // Press
                    this.changeBackground(this.normalToPressedBackground); break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: // Release
                    this.changeBackground(this.pressedToNormalBackground); break;
            }
        }
        if(this.onTouchListener !=null){ return this.onTouchListener.onTouch(v, event); }
        return false;
    }

    @Override
    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        OAnimator.animateAlphaChange(OConstants.AnimationTime, (this.isEnabled())? OConstants.Alpha_Normal:OConstants.Alpha_Disable, null, this);
    }

    protected Drawable getBackgroundById(int id) { return this.getContext().getDrawable(this.attributeArray.getResourceId(id, -1)); }
    protected abstract void changeBackground(Drawable background);
    protected abstract void stopNowAnimation();
}
