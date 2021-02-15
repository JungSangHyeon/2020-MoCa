package com.onandon.moca.onAndOn.oButton.oActionButton;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

public class OVectorActionButton extends OActionButton {

    // Constructor
    public OVectorActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeBackground(Drawable background) {
        this.setBackground(background);
    }

    @Override
    protected void stopNowAnimation() {
    }
}
