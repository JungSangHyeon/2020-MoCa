package com.onandon.moca.view.alarm.setting;

import java.io.Serializable;

public class SaveFlag implements Serializable {

    private boolean isSaved = false;

    public boolean isSaved() { return isSaved; }
    public void setSaved(boolean saved) { isSaved = saved; }
}
