package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oActionButton;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OVectorAnimationActionButton extends OActionButton {

    // Constructor
    public OVectorAnimationActionButton(Context context, AttributeSet attrs) {
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
        ((AnimatedVectorDrawable) (this.isPressed()? this.normalToPressedBackground:this.pressedToNormalBackground)).stop();
    }
}
