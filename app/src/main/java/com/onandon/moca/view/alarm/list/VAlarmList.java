package com.onandon.moca.view.alarm.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onandon.moca.R;
import com.onandon.moca.control.MViewModel;
import com.onandon.moca.onAndOn.customView.OMovableFloatingActionButton;

public class VAlarmList extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    // Associate
        // View
        private RecyclerView recyclerView;
        private OMovableFloatingActionButton createAlarmBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_list, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Associate View
        this.recyclerView = view.findViewById(R.id.alarm_list_items).findViewById(R.id.list);
        this.createAlarmBtn =  view.findViewById(R.id.alarm_list_create);

        // Set View Callback
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        new ViewModelProvider(this.requireActivity()).get(MViewModel.class).savee();
    }

    /**
     * Update
     */
    public void update(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.recyclerView.getLayoutManager();
        int sumHeight = 0;
        for(int i=0; i<layoutManager.getItemCount(); i++){
            sumHeight += layoutManager.getChildAt(i).getHeight();
        }
        View view = getView();
        if(view !=null){
            this.createAlarmBtn.load((int) (this.recyclerView.getY()+sumHeight), view.getHeight(), (int) (this.recyclerView.getX()+this.recyclerView.getWidth()));
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