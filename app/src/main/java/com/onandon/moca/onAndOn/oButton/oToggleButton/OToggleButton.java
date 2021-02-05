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

/**
 * <p>
 * </p>
 * @version 1.0.0
 */
@RequiresApi(api = Build.VERSION_CODES.M)
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
//        this.setBackground(this.normalToCheckedBackground);
        this.changeBackground(this.checkedToNormalBackground);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) { this.onCheckedChangeListener=listener; }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(this.onCheckedChangeListener !=null){ this.onCheckedChangeListener.onCheckedChanged(buttonView, isChecked); }
        if (this.isChecked()) { Log.d("TEST", "to checked"); this.changeBackground(this.normalToCheckedBackground); }
        else {Log.d("TEST", "to normal");   this.changeBackground(this.checkedToNormalBackground); }
    }

    @Override
    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        OAnimator.animateEnableChange(this);
    }

    protected Drawable getBackgroundById(int id) { return this.getContext().getDrawable(this.attributeArray.getResourceId(id, -1)); }
    protected abstract void changeBackground(Drawable background);
}
