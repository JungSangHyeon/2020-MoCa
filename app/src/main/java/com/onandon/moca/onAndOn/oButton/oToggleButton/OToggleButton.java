package com.onandon.moca.onAndOn.oButton.oToggleButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;

import com.onandon.moca.R;
import com.onandon.moca.onAndOn.OAnimator;

public abstract class OToggleButton extends androidx.appcompat.widget.AppCompatToggleButton implements CompoundButton.OnCheckedChangeListener {

    // Attribute
    protected Drawable normalToCheckedBackground, checkedToNormalBackground;

    // Associate
    private OnCheckedChangeListener onCheckedChangeListener;
    private TypedArray attributeArray;

    // Constructor
    public OToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // get attribute array
        this.attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OButton, 0, 0);

        // set attribute
        this.normalToCheckedBackground = this.getBackgroundById(R.styleable.OButton_obutton_normal_to_checked);
        this.checkedToNormalBackground = this.getBackgroundById(R.styleable.OButton_obutton_checked_to_normal);

        // set listener for change background
        super.setOnCheckedChangeListener(this);

        // set default background
        this.changeBackground(this.checkedToNormalBackground);
        this.stopNowAnimation();
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) { this.onCheckedChangeListener=listener; }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(this.onCheckedChangeListener !=null){ this.onCheckedChangeListener.onCheckedChanged(buttonView, isChecked); }
        if (this.isChecked()) { this.changeBackground(this.normalToCheckedBackground); }
        else {this.changeBackground(this.checkedToNormalBackground); }
    }

    @Override
    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        OAnimator.animateAlphaChange(300, (this.isEnabled())? 1:0.3f, null, this);
    }
    public void setCheckedWithoutAnimation(boolean checked){
        this.setChecked(checked);
        // On Checked Change()
        this.stopNowAnimation();
    }

    protected Drawable getBackgroundById(int id) { return this.getContext().getDrawable(this.attributeArray.getResourceId(id, -1)); }
    protected abstract void changeBackground(Drawable background);
    protected abstract void stopNowAnimation();
}
