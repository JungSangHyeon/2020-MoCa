package com.onandon.moca.onAndOn.oButton.oActionButton;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import com.onandon.moca.onAndOn.oBackgroundSetter.OAnimatedVectorDrawableBackgroundChanger;
import com.onandon.moca.onAndOn.oBackgroundSetter.OAnimationDrawableBackgroundChanger;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OFrameAnimationActionButton extends OActionButton {

    // Constructor
    public OFrameAnimationActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeBackground(Drawable background) {
        OAnimationDrawableBackgroundChanger.changeBackground(this, background);
    }
    @Override
    protected void stopNowAnimation() {
        ((AnimationDrawable) (this.isPressed()? this.normalToPressedBackground:this.pressedToNormalBackground)).stop();
    }
}
