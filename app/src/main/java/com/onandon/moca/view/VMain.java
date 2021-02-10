//package com.onandon.moca.view;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.onandon.moca.R;
//
//public class VMain {
//
//    // Association
//    private AppCompatActivity mainActivity;
//
//    // Constructor
//    public VMain(AppCompatActivity mainActivity) { this.mainActivity = mainActivity; }
//
//    public void onCreate(){
//        NavHostFragment navHostFragment = (NavHostFragment) this.mainActivity.getSupportFragmentManager().findFragmentById(R.id.main_view);
//        NavController navCo = navHostFragment.getNavController();
//        this.mainActivity.findViewById(R.id.main_alarmbutton).setOnClickListener((v)->{ navCo.navigate(R.id.action_global_main_alarm); });
//        this.mainActivity.findViewById(R.id.main_worldtimebutton).setOnClickListener((v)->{ navCo.navigate(R.id.action_global_main_worldtime); });
//        this.mainActivity.findViewById(R.id.main_stopwatchbutton).setOnClickListener((v)->{ navCo.navigate(R.id.action_global_main_stopwatch); });
//    }
//}
//
