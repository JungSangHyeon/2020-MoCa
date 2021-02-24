package com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onandon.moca.R;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oActionButton.OVectorAnimationActionButton;

public class OTitleInfoButtonView extends LinearLayout {

    // Associate
    private TextView title, info;
    private OVectorAnimationActionButton button;

    // Constructor
    public OTitleInfoButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Get Attribute
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OTitleInfoButtonView, 0, 0);
        String titleText = attributeArray.getString(R.styleable.OTitleInfoButtonView_tib_title);

        // add title_info layout
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alarmSettingItemLayout = (ViewGroup) layoutInflater.inflate(R.layout.titleinfobutton, this, false);
        this.addView(alarmSettingItemLayout);

        // Associate View
        this.title = alarmSettingItemLayout.findViewById(R.id.tib_title);
        this.info = alarmSettingItemLayout.findViewById(R.id.tib_info);
        this.button = alarmSettingItemLayout.findViewById(R.id.tib_button);

        // Set Attribute Of Associate
        this.title.setText(titleText);
    }

    public TextView getInfo() { return info; }
    public OVectorAnimationActionButton getButton() { return button; }
}
