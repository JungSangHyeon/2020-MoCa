package com.onandon.moca.view.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.onandon.moca.R;

public class MyToggleButton extends androidx.appcompat.widget.AppCompatToggleButton {

    private int index;

    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyToggleButton, 0, 0);
        this.index = attributes.getInteger(R.styleable.MyToggleButton_index, -1);
    }

    public int getIndex() { return index; }
}
