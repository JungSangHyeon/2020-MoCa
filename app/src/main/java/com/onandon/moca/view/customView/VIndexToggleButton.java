package com.onandon.moca.view.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.onandon.moca.R;

public class VIndexToggleButton extends androidx.appcompat.widget.AppCompatToggleButton {

    // Attribute
    private int index;

    // Constructor
    public VIndexToggleButton(Context context, AttributeSet attrs) { super(context, attrs); }

    // Getter & Setter
    public int getIndex() { return index; }
    public void setIndex(int i) { this.index=i; }
}
