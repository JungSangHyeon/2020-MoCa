package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oActionButton;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

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
