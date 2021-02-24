package com.onandon.moca.onAndOnAsset.view.comp.fragment.weekSelect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.onandon.moca.onAndOnAsset.model.OViewModel;

public class WeekListViewModel extends ViewModel implements OViewModel {
    private final MutableLiveData<boolean[]> weeks = new MutableLiveData<>();

    public WeekListViewModel() {
//        weeks.setValue(new boolean[7]);
    }

    public void setWeeks(boolean[] weeks) {
        this.weeks.setValue(weeks);
    }

    public LiveData<boolean[]> getWeeks() {
        return weeks;
    }

    @Override
    public LiveData getLiveData() {
        return getWeeks();
    }
}
