package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class OFrameAnimationToggleButton extends OToggleButton {

    // Constructor
    public OFrameAnimationToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeBackground(Drawable background) {
        AnimationDrawable nowBackground = (AnimationDrawable) this.getBackground();
        nowBackground.stop();
        nowBackground.selectDrawable(0);

        AnimationDrawable newBackground = (AnimationDrawable) background;
        this.setBackground(newBackground);
        newBackground.start();
    }

    @Override
    protected void stopNowAnimation() {
        ((AnimationDrawable) (this.isChecked()? this.normalToCheckedBackground:this.checkedToNormalBackground)).stop();
    }
}
