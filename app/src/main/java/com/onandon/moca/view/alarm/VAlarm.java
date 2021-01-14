package com.onandon.moca.view.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.NavHostFragment;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.utility.ObjectAndByteArrayConverter;
import com.onandon.moca.view.alarm.list.VAlarmList;
import com.onandon.moca.view.alarm.setting.VAlarmSetting;

import java.util.Locale;

public class VAlarm extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm, container, false);
    }
}
