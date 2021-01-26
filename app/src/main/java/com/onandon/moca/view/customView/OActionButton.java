package com.onandon.moca.view.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.onandon.moca.R;

public class OActionButton extends androidx.appcompat.widget.AppCompatButton {

    private VectorDrawable defaultStateBackground, defaultPressedBackground, disabledBackground;

    // Constructor
    public OActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OActionButton, 0, 0);
        this.defaultStateBackground = this.getDrawable(context, array, R.styleable.OActionButton_actionbutton_default_state_background);
        this.defaultPressedBackground = this.getDrawable(context, array, R.styleable.OActionButton_actionbutton_default_pressed_background);
        this.disabledBackground = this.getDrawable(context, array, R.styleable.OActionButton_actionbutton_disabled_background);

        this.setBackground(this.defaultStateBackground);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.isEnabled()){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    this.setBackground(this.defaultPressedBackground);
                    break;
                case MotionEvent.ACTION_UP:
                    this.setBackground(this.defaultStateBackground);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private VectorDrawable getDrawable(Context context, TypedArray array, int id) {
        return (VectorDrawable) context.getDrawable(array.getResourceId(id, -1));
    }

    @Override
    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        if(this.isEnabled()){ this.setBackground(this.defaultStateBackground); } // disable -> enable
        else{ this.setBackground(this.disabledBackground); } // enable -> disable
    }
}
