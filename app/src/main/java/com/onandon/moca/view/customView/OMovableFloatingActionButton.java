package com.onandon.moca.view.customView;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OMovableFloatingActionButton extends FloatingActionButton  {

    public OMovableFloatingActionButton(Context context, AttributeSet attrs) { super(context, attrs); }

    public void save() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("OMovableFloatingActionButton", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("x", this.getX()-this.getLeft());
        editor.putFloat("y", this.getY()-this.getTop());
        editor.commit();
    }

    public void load() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("OMovableFloatingActionButton", Context.MODE_PRIVATE);
        float x = prefs.getFloat("x", -1);
        float y = prefs.getFloat("y", -1);
        if(x!=-1 && y!=-1){
            this.setX(x);
            this.setY(y);
        }
    }

}