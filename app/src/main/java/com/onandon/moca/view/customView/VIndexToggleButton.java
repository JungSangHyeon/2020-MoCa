package com.onandon.moca.view.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.onandon.moca.R;

public class VIndexToggleButton extends androidx.appcompat.widget.AppCompatToggleButton {

    private int index;

    public VIndexToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyToggleButton, 0, 0);
//        this.index = attributes.getInteger(R.styleable.MyToggleButton_index, -1);
    }

//    public VIndexToggleButton(@NonNull Context context) { super(context); }

    public int getIndex() { return index; }
    public void setIndex(int i) { this.index=i; }
}
