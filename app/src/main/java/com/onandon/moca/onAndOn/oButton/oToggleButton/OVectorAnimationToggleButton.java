package com.onandon.moca.onAndOn.oButton.oToggleButton;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.onAndOn.oBackgroundSetter.OAnimatedVectorDrawableBackgroundChanger;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OVectorAnimationToggleButton extends OToggleButton {

    // Constructor
    public OVectorAnimationToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeBackground(Drawable background) {
        OAnimatedVectorDrawableBackgroundChanger.changeBackground(this, background);
    }

    @Override
    protected void stopNowAnimation() {
        ((AnimatedVectorDrawable) (this.isChecked()? this.normalToCheckedBackground:this.checkedToNormalBackground)).stop();
    }
}
