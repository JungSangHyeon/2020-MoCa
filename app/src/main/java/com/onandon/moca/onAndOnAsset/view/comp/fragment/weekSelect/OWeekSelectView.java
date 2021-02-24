package com.onandon.moca.onAndOnAsset.view.comp.fragment.weekSelect;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;

import com.onandon.moca.R;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.OFragment;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OIndexToggleButton;

import java.util.Calendar;

public class OWeekSelectView extends OFragment<WeekListViewModel> implements CompoundButton.OnCheckedChangeListener {

    // Working Variable
    private int numberOfDaysChecked;

    // Component
    private OIndexToggleButton[] checkBoxes;

    /**
     * System Callback
     */
    @Override protected void createComponent() { this.checkBoxes = new OIndexToggleButton[Calendar.DAY_OF_WEEK]; }
    @Override public void onCreate(Activity activity) { }
    @Override public ViewModelStoreOwner getViewModelStoreOwner() { return this.requireParentFragment(); }
    @Override public Class<? extends ViewModel> getModel() { return WeekListViewModel.class; }
    @Override public int getLayoutId() { return R.layout.alarm_setting_weekdays; }
    @Override
    public void associateAndInitView(View view) {
        this.checkBoxes[Calendar.SUNDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_0);
        this.checkBoxes[Calendar.MONDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_1);
        this.checkBoxes[Calendar.TUESDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_2);
        this.checkBoxes[Calendar.WEDNESDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_3);
        this.checkBoxes[Calendar.THURSDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_4);
        this.checkBoxes[Calendar.FRIDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_5);
        this.checkBoxes[Calendar.SATURDAY-1] = view.findViewById(R.id.alarm_setting_weekdays_6);

        int index=0;
        for (OIndexToggleButton checkBox: this.checkBoxes) { // init buttons
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setIndex(index++);
        }
    }

    /**
     * Update
     */
    @Override
    protected void update() {
        this.numberOfDaysChecked = 0;
        boolean[] weeks = this.model.getWeeks().getValue();
        for (OIndexToggleButton checkBox: this.checkBoxes) {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setCheckedWithoutAnimation(weeks[checkBox.getIndex()]);
            checkBox.setOnCheckedChangeListener(this);
            if(checkBox.isChecked()){this.numberOfDaysChecked++;}
        }
    }

    /**
     * Callback
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        OIndexToggleButton vIndexToggleButton = (OIndexToggleButton) compoundButton;
        if (this.numberOfDaysChecked==1 && !isChecked) { // if no checkbox is checked
            vIndexToggleButton.setChecked(true); // recheck current checkbox
        } else { // at least one be checked
            boolean[] weeks = this.model.getWeeks().getValue();
            if (weeks[vIndexToggleButton.getIndex()] != isChecked) {
                this.numberOfDaysChecked = (isChecked) ? this.numberOfDaysChecked + 1 : this.numberOfDaysChecked - 1;
                weeks[vIndexToggleButton.getIndex()] = isChecked;
                this.model.setWeeks(weeks);
            }
        }
    }
}
