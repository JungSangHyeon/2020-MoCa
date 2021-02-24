package com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton;

import android.content.Context;
import android.util.AttributeSet;

public class OIndexToggleButton extends OVectorAnimationToggleButton {

    // Attribute
    private int index;

    // Constructor
    public OIndexToggleButton(Context context, AttributeSet attrs) { super(context, attrs); }

    // Getter & Setter
    public int getIndex() { return index; }
    public void setIndex(int i) { this.index=i; }
}
