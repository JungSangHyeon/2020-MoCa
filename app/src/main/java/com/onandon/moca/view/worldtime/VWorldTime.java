package com.onandon.moca.view.worldtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.onandon.moca.R;

public class VWorldTime extends Fragment {

//    public VWorldTime(AppCompatActivity mainActivity) {
//        super(R.layout.worldtime);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.worldtime, container, false);
    }
}
