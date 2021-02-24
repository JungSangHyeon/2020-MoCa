package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oActionButton;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OFrameAnimationActionButton extends OActionButton {

    // Constructor
    public OFrameAnimationActionButton(Context context, AttributeSet attrs) {
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
        ((AnimationDrawable) (this.isPressed()? this.normalToPressedBackground:this.pressedToNormalBackground)).stop();
    }
}
