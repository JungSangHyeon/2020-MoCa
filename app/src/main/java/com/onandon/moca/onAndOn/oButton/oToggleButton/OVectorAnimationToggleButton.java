package com.onandon.moca.onAndOn.oButton.oToggleButton;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
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
//        OAnimatedVectorDrawableBackgroundChanger.changeBackground(this, background);
        AnimatedVectorDrawable newBackground = (AnimatedVectorDrawable) background;
        this.setBackground(newBackground);
        newBackground.start();
    }
}
