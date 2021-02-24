package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class OVectorAnimationToggleButton extends OToggleButton {

    // Constructor
    public OVectorAnimationToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeBackground(Drawable background) {
        AnimatedVectorDrawable newBackground = (AnimatedVectorDrawable) background;
        this.setBackground(newBackground);
        newBackground.start();
    }

    @Override
    protected void stopNowAnimation() {
        ((AnimatedVectorDrawable) (this.isChecked()? this.normalToCheckedBackground:this.checkedToNormalBackground)).stop();
    }
}
