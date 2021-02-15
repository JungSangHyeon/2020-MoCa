package com.onandon.moca.onAndOn.compoundView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onandon.moca.R;

public class OTitleInfoView extends LinearLayout {

    // Associate
    private TextView title, info;

    // Constructor
    public OTitleInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Get Attribute
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OTitleInfoView, 0, 0);
        String titleText = attributeArray.getString(R.styleable.OTitleInfoView_ti_title);

        // add title_info layout
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alarmSettingItemLayout = (ViewGroup) layoutInflater.inflate(R.layout.titleinfo, this, false);
        this.addView(alarmSettingItemLayout);

        // Associate View
        this.title = alarmSettingItemLayout.findViewById(R.id.ti_title);
        this.info = alarmSettingItemLayout.findViewById(R.id.ti_info);

        // Set Attribute Of Associate
        this.title.setText(titleText);
    }

    public TextView getInfo() { return info; }
}
