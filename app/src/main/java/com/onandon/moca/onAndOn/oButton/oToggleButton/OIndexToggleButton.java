package com.onandon.moca.onAndOn.oButton.oToggleButton;

import android.content.Context;
import android.util.AttributeSet;

import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;

public class OIndexToggleButton extends OVectorAnimationToggleButton {

    // Attribute
    private int index;

    // Constructor
    public OIndexToggleButton(Context context, AttributeSet attrs) { super(context, attrs); }

    // Getter & Setter
    public int getIndex() { return index; }
    public void setIndex(int i) { this.index=i; }
}
