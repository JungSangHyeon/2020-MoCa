package com.onandon.moca.domain.view.alarm.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.OFragment;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.OMovableFloatingActionButton;

public class VAlarmMain extends OFragment<AlarmViewModel> implements ViewTreeObserver.OnGlobalLayoutListener {

    // Associate
        // View
        private View dashboardView;
        private RecyclerView recyclerView;
        private OMovableFloatingActionButton createAlarmBtn;

    /**
     * System Callback
     */
    @Override protected void createComponent() { }
    @Override public void onCreate(Activity activity) { }
    @Override public ViewModelStoreOwner getViewModelStoreOwner() { return this.requireActivity(); }
    @Override public Class<? extends ViewModel> getModel() { return AlarmViewModel.class; }
    @Override public int getLayoutId() { return R.layout.alarm_main; }
    @Override
    public void associateAndInitView(View view) {
        this.dashboardView = view.findViewById(R.id.alarm_list_dashboard);
        this.recyclerView = view.findViewById(R.id.alarm_list_items).findViewById(R.id.list);
        this.createAlarmBtn =  view.findViewById(R.id.alarm_list_create);

        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.createAlarmBtn.setOnClickListener((v)->this.goToAlarmSettingForCreateAlarm());
    }
    @Override
    public void onGlobalLayout() { // 사용자에게 화면이 보여질 때. onPreDraw 사용해 볼 것.
        this.update();
    }
    @Override
    public void onPause() {
        super.onPause();
        this.createAlarmBtn.save();
    }

    /**
     * Update
     */
    @Override
    public void update(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.recyclerView.getLayoutManager();
        int sumHeight = 0;
        for(int i=0; i<layoutManager.getChildCount(); i++){ sumHeight += layoutManager.getChildAt(i).getHeight(); }
        if(this.getView() !=null){
            this.createAlarmBtn.load(
                    this.dashboardView.getHeight() + sumHeight,
                    this.getView().getHeight(),
                    (int) (this.recyclerView.getX()+this.recyclerView.getWidth())
            );
        }
    }

    /**
     * Callback
     */
    private void goToAlarmSettingForCreateAlarm() {
        Bundle bundle = new Bundle();
        bundle.putInt("targetIndex", -1);
        Navigation.findNavController(this.getView()).navigate(R.id.action_VAlarmList_to_VAlarmSetting, bundle);
    }
}