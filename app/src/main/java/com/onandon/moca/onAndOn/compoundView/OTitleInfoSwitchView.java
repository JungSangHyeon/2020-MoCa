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
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;

public class OTitleInfoSwitchView extends LinearLayout implements View.OnClickListener {

    private final int NoAttribute = -1;

    // Associate
    private TextView title;
    private LinearLayout settingLayoutContainer;
    private View settingLayout;
    private OVectorAnimationToggleButton onOffButton;

    // Constructor
    public OTitleInfoSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Get Attribute
        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AlarmSettingItem, 0, 0);
        String titleText = attributeArray.getString(R.styleable.AlarmSettingItem_alarm_setting_item_title);
        int settingLayoutId = attributeArray.getResourceId(R.styleable.AlarmSettingItem_alarm_setting_item_info, this.NoAttribute);

        // add alarm_setting_item layout
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alarmSettingItemLayout = (ViewGroup) layoutInflater.inflate(R.layout.alarm_setting_item, this, false);
        this.addView(alarmSettingItemLayout);

        // Associate View
        this.title = alarmSettingItemLayout.findViewById(R.id.alarm_setting_item_title);
        this.settingLayoutContainer = alarmSettingItemLayout.findViewById(R.id.alarm_setting_item_setting_layout_container);
        this.onOffButton = alarmSettingItemLayout.findViewById(R.id.alarm_setting_item_on_off_button);

        // Set Attribute Of Associate
        this.title.setOnClickListener(this);
        this.title.setText(titleText);
        if(settingLayoutId!=this.NoAttribute){
            this.settingLayout = layoutInflater.inflate(settingLayoutId, this, false);
            this.settingLayoutContainer.addView(this.settingLayout);
        }
    }

    @Override
    public void onClick(View v) {
        if(this.onOffButton.isEnabled()){
            this.onOffButton.setChecked(!this.onOffButton.isChecked());
        }
    }

    // Getter
    public TextView getTitleTextView() { return title; }
    public View getSettingLayout() { return settingLayout; }
    public OVectorAnimationToggleButton getOnOffButton() { return onOffButton; }
}
