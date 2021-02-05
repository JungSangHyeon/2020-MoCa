package com.onandon.moca.onAndOn.oButton.oToggleButton;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import com.onandon.moca.onAndOn.oBackgroundSetter.OAnimationDrawableBackgroundChanger;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OFrameAnimationToggleButton extends OToggleButton {

    // Constructor
    public OFrameAnimationToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeBackground(Drawable background) {
        OAnimationDrawableBackgroundChanger.changeBackground(this, background);
    }
}
